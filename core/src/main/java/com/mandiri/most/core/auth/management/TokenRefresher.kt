package com.mandiri.most.core.auth.management

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.model.AuthenticationType
import com.mandiri.most.core.auth.api.model.PinTokenRequest
import com.mandiri.most.core.auth.token.TokenStore
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.auth.token.TokenTimeInfo
import com.mandiri.most.core.common.network.MostAPIException
import com.mandiri.most.core.common.network.toMostAPIError
import com.mandiri.most.core.init.NetworkConstants
import com.mandiri.most.core.init.log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

internal typealias Callback = () -> Unit
internal typealias StrategyPredicate = (TokenTimeInfo?) -> Boolean

class TokenRefresher(
    val tokenStore: TokenStore,
    private val authRepository: AuthRepository,
    private val configuration: TokenRefresherConfiguration = TokenRefresherConfiguration()
) {

    private val eventStream: BehaviorSubject<RefreshEvent> = BehaviorSubject.create()
    private var onForceLogout: Callback? = null
    private var timer: Disposable? = null
    private val eventDisposable: Disposable
    private val lastRefreshAttempt: MutableMap<TokenScope, Instant> = mutableMapOf()
    private val refreshStrategies: Map<TokenScope, StrategyPredicate> = mapOf(
        TokenScope.A1 to this::a1RefreshNeeded,
        TokenScope.Market to this::a2RefreshNeeded,
        TokenScope.Order to this::a2RefreshNeeded,
        TokenScope.Portfolio to this::a2RefreshNeeded,
        TokenScope.User to this::a2RefreshNeeded,
        TokenScope.Pin to this::a2RefreshNeeded
    )

    init {
        tokenStore.initialize()

        this.eventDisposable = eventStream
            .observeOn(Schedulers.io())
            .subscribe { onEvent(it) }
    }

    @Synchronized
    fun startRefreshing() {
        if(timer == null) {
            timer = Observable
                .interval(
                    configuration.startAfterDelaySec,
                    configuration.loopEverySec,
                    TimeUnit.SECONDS
                )
                .observeOn(Schedulers.io())
                .subscribe {
                    eventStream.onNext(Check)
                }
        }
    }

    @Synchronized
    fun stopRefreshing() {
        timer?.dispose()
        timer = null
    }

    fun forceRefreshA2Tokens(): Completable {
        this.stopRefreshing()
        val scopes = listOf(TokenScope.Market, TokenScope.Order, TokenScope.Portfolio, TokenScope.User)

        return authRepository
            .authorize(scopes)
            .flatMapCompletable {
                val response = it
                Completable.fromAction {
                    response.forEach {
                        val scope = TokenScope.fromRawValue(it.scope)
                        val tokenString = it.token
                        if (scope == null) log.e("received unsupported token scope for A2 refresh: ${it.scope}")
                        scope?.let {
                            log.d("force refresh: setting token for scope $scope")
                            tokenStore.setToken(scope, tokenString)
                        }
                    }
                }
            }
            .doOnError {
                log.e("error force refreshing A2 tokens: ${it.localizedMessage}")
            }
    }

    fun setForcedLogoutNotifier(callback: Callback) {
        this.onForceLogout = callback
    }

    private fun onEvent(event: RefreshEvent) {
        log.d("TokenRefresher got event $event")
        when(event) {
            is Check -> onCheckEvent()
            is RefreshToken -> onRefreshTokenEvent(event.scope)
            is RefreshTokenSuccess -> {
                tokenStore.setToken(event.scope, event.jwtToken)
                this.lastRefreshAttempt[event.scope] = event.instant
            }
            is RefreshTokenFailure -> {
                this.lastRefreshAttempt[event.scope] = event.instant
            }
            is ForcedLogout -> {
                this.onForceLogout?.invoke()
            }
        }
    }

    /**
     * Retrieves the timing information of existing tokens from the token store
     * Uses the refreshStrategy appropriate for each token scope
     * If a refresh is needed, enqueues a refreshToken event
     */
    private fun onCheckEvent() {
        if(tokenStore.loginStatus() == AuthenticationType.None) {
            return
        }
        val timeInfos = tokenStore.getTokensTimeInfo()
        TokenScope.values().map {
            val scope = it
            val info = timeInfos.firstOrNull { it.scope == scope }
            val decision = refreshStrategies[scope]?.invoke(info) ?: false
            if (decision) {
                log.d("decided to refresh token for scope $scope")
                eventStream.onNext(RefreshToken(scope))
            }
        }
    }

    /**
     * Starts the right refresh process given the token scope:
     * - for A1Token, just do a normal A1 refresh
     * - for pin, do a pin exchange API call (but only if a pin has been set in the token store)
     * - otherwise, do an authorization call to get an A2 token
     * The result of the API call will eventually result in an event being enqueued, either refreshSuccess or refreshFailure
     * Note the special handling of a forcedLogout response. These responses are only communicate by the backend during
     * one of these token API calls. A forcedLogout event is enqueued
     */
    private fun onRefreshTokenEvent(scope: TokenScope) {
        val op = when(scope) {
            TokenScope.A1 -> authRepository.a1Refresh()
            TokenScope.Pin -> {
                val pin = tokenStore.getPin()
                pin?.let {
                    authRepository.pinToken(PinTokenRequest(pin, 5))
                }
            }
            else -> authRepository
                .authorize(listOf(scope))
                .flatMap {
                    when(it.size) {
                        0 -> Single.error(MostAPIException.Unknown("got 0 tokens back!"))
                        else -> Single.just(it[0])
                    }
                }
        }

        op
            ?.observeOn(Schedulers.io())
            ?.subscribe(
                {
                    eventStream.onNext(RefreshTokenSuccess(scope, it.token, Instant.now()))
                },
                {
                    val mostException = it.toMostAPIError()
                    log.d("TokenRefresher on refresh error $mostException")
                    if(mostException == MostAPIException.ForcedLogout) {
                        eventStream.onNext(ForcedLogout)
                    } else {
                        eventStream.onNext(RefreshTokenFailure(scope, Instant.now()))
                    }
                }
            )
    }

    /**
     * Determines whether the A1 token needs to be refreshed
     */
    private fun a1RefreshNeeded(info: TokenTimeInfo?): Boolean {

        if(info == null) return false

        val last = lastRefreshAttempt[info.scope]

        if(last != null && Duration.between(last, Instant.now()).seconds <= configuration.a1RefreshMinDelaySec)
            return false

        if(info.secondsSinceIssue >= configuration.a1RefreshMaxWaitSec) {
            log.d("refresh a1 token due to ${info.secondsSinceIssue} seconds since issuance ")
            return true
        }
        if(info.secondsUntilExpiry <= configuration.a1ForceRefreshWhenSecLeft) {
            log.d("refresh a1 token due to coming expiry in ${info.secondsUntilExpiry} seconds ")
            return true
        }
        return false
    }

    /**
     * Determines whether an A2 (and PIN) token needs to be refreshed
     */
    private fun a2RefreshNeeded(info: TokenTimeInfo?): Boolean {

        if(info == null) return true

        if(info.isExpired) {
            log.d("refresh a2/pintoken ${info.scope} due to expiry")
            return true
        }
        if(info.secondsUntilExpiry <= configuration.a2ForceRefreshWhenSecLeft) {
            log.d("refresh a2/pintoken ${info.scope} due to ${info.secondsUntilExpiry} seconds left")
            return true
        }
        return false
    }

    protected fun finalize() {
        eventDisposable.dispose()
        timer?.dispose()
    }
}

data class TokenRefresherConfiguration(
    val a1RefreshMinDelaySec: Long = NetworkConstants.A1_REFRESH_MIN_DELAY_SEC,
    val a1RefreshMaxWaitSec: Long = NetworkConstants.A1_REFRESH_MAX_WAIT_SEC,
    val a1ForceRefreshWhenSecLeft: Long = NetworkConstants.A1_REFRESH_MAX_WAIT_SEC,
    val a2ForceRefreshWhenSecLeft: Long = NetworkConstants.A2_FORCE_REFRESH_WHEN_SEC_LEFT,
    val startAfterDelaySec: Long = 0,
    val loopEverySec: Long = 60
)

private sealed class RefreshEvent
private object Check: RefreshEvent()
private object ForcedLogout: RefreshEvent()
private data class RefreshToken(val scope: TokenScope): RefreshEvent()
private data class RefreshTokenSuccess(
    val scope: TokenScope,
    val jwtToken: String,
    val instant: Instant
): RefreshEvent()
private data class RefreshTokenFailure(
    val scope: TokenScope,
    val instant: Instant
): RefreshEvent()

