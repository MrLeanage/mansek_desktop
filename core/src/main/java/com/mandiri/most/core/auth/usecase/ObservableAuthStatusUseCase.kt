package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.model.AuthenticationType
import com.mandiri.most.core.auth.management.TokenRefresher
import com.mandiri.most.core.auth.token.JWTToken
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.common.domain.ObservableUseCase
import com.mandiri.most.core.common.network.MostAPIException
import com.mandiri.most.core.common.network.toMostAPIError
import com.mandiri.most.core.init.log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 *
 *  This class provides a unified view of the authentication lifecycle.
 *  It is recommended to create this use case early when the application starts
 *  (e.g. before displaying the main page)
 *
 *  The very first AuthStatus that will be observed will be one of the following:
 *  1. the user is not logged in
 *  This can happen if the user
 *      - has not logged in in the past
 *      - the user token has now expired
 *      - the user was logged out by logging into another device while the app was inactive
 *  The app can then continue by logging in as guest or user
 *  Logging in as guest does not require user interaction
 *
 *  2. the user is still validly logged in as a guest or as a user
 * The app can proceed to display any user relevant pages
 *
 * FORCED LOGOUT
 *  From this point on, at any time, the observer may get an AuthStatus that the user
 *  has logged in on another device (ForcedLogout).
 *  This MUST be handled by the app, since at this point, most of the app functionality is disabled.
 *  The user can probably be given the choice to login again (or continue as guest)
 *  The app is therefore expected to either login the user again or continue as guest.
 *  Note: in this state, no new data can be fetched and real time data may stop at any time.
 *  Consider this when designing the UX
 *
 * Operations provided by this class:
 * 1. login as guest
 * the observer will either get a LoggedinAsGuest or ErrorLogin
 * The observer is free to try again later
 *
 * 2. login as user
 * The observer will either get a LoggedinAsGuest or ErrorLogin
 * The observer is free to try again later
 *
 * 3. Logout
 * Use this to explicitely logout a user
 * Logging out as a guest is supported but optional.
 *
 * NOTE: there is no need to logout to transition between being logged in as guest and
 * wanting to login as a user. Just directly request a login as user.
 *
 *
 */

class ObservableAuthStatusUseCase(
    private val refresher: TokenRefresher,
    private val authRepository: AuthRepository

): ObservableUseCase<Unit, AuthAction, AuthStatus, Observable<AuthStatus>?>() {

    private var handler: AuthLifeCycleHandler? = null

    override fun build(params: Unit): Observable<AuthStatus> {
        val newHandler = AuthLifeCycleHandler(refresher, authRepository)
        handler = newHandler
        newHandler.start()
        return newHandler.getStatusStream()
    }

    override fun send(params: AuthAction): Observable<AuthStatus>? {
        when(params) {
            is AuthAction.LoginAsUser -> handler?.login(params.credential)
            is AuthAction.LoginAsGuest -> handler?.loginAsGuest()
            is AuthAction.Logout -> handler?.logout()
        }
        return handler?.getStatusStream()
    }
}

sealed class AuthStatus {
    object NotLoggedIn: AuthStatus()
    object LoggingIn: AuthStatus()
    object LoggedInAsUser: AuthStatus()
    object LoggedInAsGuest: AuthStatus()
    data class ErrorLogin(val error: MostAPIException): AuthStatus()
    object ForcedLogout: AuthStatus()
}

sealed class AuthAction {
    object LoginAsGuest: AuthAction()
    class LoginAsUser(val credential: UserCredential): AuthAction()
    object Logout: AuthAction()
}


private class AuthLifeCycleHandler(
    val refresher: TokenRefresher,

    val authRepository: AuthRepository

) {

    private val statusStream: BehaviorSubject<AuthStatus> = BehaviorSubject.create()

    private val eventStream: BehaviorSubject<Event> = BehaviorSubject.create()
    private val eventSubscription: Disposable

    init {
        refresher.stopRefreshing()
        eventSubscription = eventStream
            .observeOn(Schedulers.io())
            .subscribe(this::onEvent)
    }

    fun getStatusStream(): Observable<AuthStatus> {
        return statusStream.distinctUntilChanged()
    }

    fun start() {
        eventStream.onNext(Event.Initialize)
    }

    fun login(credential: UserCredential) {
        eventStream.onNext(Event.LoginRequest(credential))
    }

    fun loginAsGuest() {
        eventStream.onNext(Event.LoginRequest(null))
    }

    fun logout() {
        eventStream.onNext(Event.LogoutRequest)
    }

    private fun onEvent(event: Event) {
        log.d("authStatusUseCase: processing event $event")
        when(event) {
            is Event.Initialize -> doInitialize()
            is Event.A1TokenValid -> doInitialAuthSuccessTasks(event.newA1JwtToken)
            is Event.A1TokenNotValid -> emitStatus(AuthStatus.NotLoggedIn)
            is Event.A1TokenForcedLogout -> doNotifyForceLogout()
            is Event.LoginRequest -> doLogin(event.credential)
            is Event.LoginSuccess -> doLoginSuccessTasks(event.newA1JwtToken)
            is Event.LoginError -> doLoginFailed(event.error)
            is Event.LoginComplete -> doLoginComplete()
            is Event.LogoutRequest -> doLogout()
        }
    }

    private fun doInitialize() {
        val currentState = refresher.tokenStore.loginStatus()

        when(currentState) {
            AuthenticationType.None -> emitStatus(AuthStatus.NotLoggedIn)
            AuthenticationType.User -> doValidateA1TokenValid()
            AuthenticationType.Guest -> doValidateA1TokenValid()
        }
    }

    /**
     * Let's check if the current A1Token we have is still valid
     * (it could be expired, or it may have been revoked server side)
     */
    private fun doValidateA1TokenValid() {
        //the token may be expired or forced log out, treat all errors the same
        authRepository
            .a1Refresh()
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    eventStream.onNext(Event.A1TokenValid(it.token))
                },
                {
                    log.d("authStatusUseCase: doValidateA1TokenValid() error $it")
                    eventStream.onNext(Event.A1TokenNotValid)
                }
            )
    }

    /**
     * This method is invoked when the app is already logged in, and the A1Token is still valid
     *
     */
    private fun doInitialAuthSuccessTasks(newA1JwtToken: JWTToken) {
        log.d("authStatusUseCase: doInitialAuthSuccessTasks()")
        val ok = refresher.tokenStore.setToken(TokenScope.A1, newA1JwtToken)
        if(!ok) {
            // got a bad token from server -> ignore and set as not logged in
            emitStatus(AuthStatus.NotLoggedIn)
            refresher.tokenStore.deleteAllTokens()
            return
        }
        refresher.setForcedLogoutNotifier(this::notifyOnOtherDeviceUserSignOn)
        refresher.startRefreshing()
        doPublishLoginStatus()
    }

    /**
     * Upon a new successful login, we want to clear any past token information,
     * set the new A1 Token, and force a refresh of all A2tokens before
     * returning to the caller
     */
    private fun doLoginSuccessTasks(newA1JwtToken: JWTToken) {
        log.d("authStatusUseCase: doLoginSuccessTasks()")
        refresher.stopRefreshing()
        refresher.tokenStore.deleteAllTokens()
        val ok = refresher.tokenStore.setToken(TokenScope.A1, newA1JwtToken)
        if(!ok) {
            //got a bad token from the server, should not happen unless some tampering is going on
            refresher.tokenStore.deleteAllTokens()
            emitStatus(AuthStatus.ErrorLogin(MostAPIException.BadToken))
            doPublishLoginStatus()
            return
        }
        refresher
            .forceRefreshA2Tokens()
            .subscribe(
                {
                    eventStream.onNext(Event.LoginComplete)
                },
                {
                    eventStream.onNext(Event.LoginComplete)
                }
            )
    }

    /**
     * We've now logged in, and refreshed A2Tokens
     */
    private fun doLoginComplete() {
        refresher.setForcedLogoutNotifier(this::notifyOnOtherDeviceUserSignOn)
        refresher.startRefreshing()
        doPublishLoginStatus()
    }

    private fun doLogin(credential: UserCredential?) {
        emitStatus(AuthStatus.LoggingIn)
        val operation =
            if(credential == null)
                authRepository.loginAsGuest()
            else
                authRepository.doLogin(credential)

        operation
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    eventStream.onNext(Event.LoginSuccess(it.token))
                },
                {
                    log.d("authStatusUseCase: doLogin() error $it")
                    eventStream.onNext(Event.LoginError(it.toMostAPIError()))
                }
            )
    }

    private fun doNotifyForceLogout() {
        emitStatus(AuthStatus.ForcedLogout)
    }

    private fun doLogout() {
        refresher.stopRefreshing()
        refresher.tokenStore.deleteAllTokens()
        emitStatus(AuthStatus.NotLoggedIn)
    }

    private fun doLoginFailed(error: MostAPIException) {
        emitStatus(AuthStatus.ErrorLogin(error))
        doPublishLoginStatus()
    }

    private fun doPublishLoginStatus() {
        val status = refresher.tokenStore.loginStatus()
        log.d("publishing status: $status")
        when(status) {
            AuthenticationType.None -> emitStatus(AuthStatus.NotLoggedIn)
            AuthenticationType.User -> emitStatus(AuthStatus.LoggedInAsUser)
            AuthenticationType.Guest -> emitStatus(AuthStatus.LoggedInAsGuest)
        }
    }

    private fun emitStatus(status: AuthStatus) {
        log.d("emitting status $status")
        statusStream.onNext(status)
    }

    fun notifyOnOtherDeviceUserSignOn() {
        eventStream.onNext(Event.A1TokenForcedLogout)
    }
}

private sealed class Event {
    object Initialize: Event()
    class A1TokenValid(val newA1JwtToken: String): Event()
    object A1TokenNotValid: Event()
    object A1TokenForcedLogout: Event()
    class LoginRequest(val credential: UserCredential?): Event()
    class LoginSuccess(val newA1JwtToken: String): Event()
    class LoginError(val error: MostAPIException): Event()
    object LoginComplete: Event()
    object LogoutRequest: Event()
}
