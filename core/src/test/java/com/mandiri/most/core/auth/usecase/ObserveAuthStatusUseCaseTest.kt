package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.auth.api.source.network.TokenInjector
import com.mandiri.most.core.auth.management.TokenRefresher
import com.mandiri.most.core.auth.management.TokenRefresherConfiguration
import com.mandiri.most.core.auth.token.DefaultTokenStore
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.auth.token.TokenStore
import com.mandiri.most.core.common.network.ApiInterceptor
import com.mandiri.most.core.init.StdOutAllLogger
import com.mandiri.most.core.init.log
import com.mandiri.most.core.util.FakeLocaleManager
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class ObserveAuthStatusUseCaseTest {

    private val mockWebServer = MockWebServer()
    private lateinit var tokenStore: TokenStore
    private lateinit var repository: AuthRepository
    private lateinit var useCase: ObservableAuthStatusUseCase
    private lateinit var tokenRefresher: TokenRefresher
    private val localeManager = FakeLocaleManager()

    @Before
    @ExperimentalSerializationApi
    fun setup() {
        log = StdOutAllLogger()
        mockWebServer.start()
        this.tokenStore = DefaultTokenStore(SimpleMemoryKeyValueStore())
        val tokenInjector = TokenInjector(tokenStore)

        val httpClient = OkHttpClient
            .Builder()
            .addInterceptor(tokenInjector)
            .addInterceptor(ApiInterceptor(localeManager))
            .build()

        val authApi =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(AuthAPI::class.java)
        this.repository = DefaultAuthRepository(authApi)
        val tokenRefresherConfiguration = TokenRefresherConfiguration(
            a1ForceRefreshWhenSecLeft = 30,
            a1RefreshMaxWaitSec = 5,
            a1RefreshMinDelaySec = 3,
            startAfterDelaySec = 1,
            loopEverySec = 1
        )
        this.tokenRefresher = TokenRefresher(tokenStore, repository, tokenRefresherConfiguration)
        useCase = ObservableAuthStatusUseCase(tokenRefresher, repository)
    }

    @Test
    fun `test not logged in`() {
        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()
        testObserver
            .awaitCount(1)
            .assertValues(AuthStatus.NotLoggedIn)
    }

    @Test
    fun `test not logged in and loginAsGuest`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300)

        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()
        useCase.send(AuthAction.LoginAsGuest)
        testObserver
            .awaitCount(2)
            .assertValues(AuthStatus.NotLoggedIn, AuthStatus.LoggedInAsGuest)

    }

    @Test
    fun `test not logged in and loginAsUser`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300)

        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()
        useCase.send(AuthAction.LoginAsUser(UserCredential("john", "1234")))
        testObserver
            .awaitCount(2)
            .assertValues(AuthStatus.NotLoggedIn, AuthStatus.LoggedInAsUser)
    }

    @Test
    fun `test not logged in, loginAsGuest, then as User`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300)

        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()
        useCase.send(AuthAction.LoginAsGuest)
        useCase.send(AuthAction.LoginAsUser(UserCredential("john", "1234")))
        testObserver
            .awaitCount(3)
            .assertValues(
                AuthStatus.NotLoggedIn,
                AuthStatus.LoggedInAsGuest,
                AuthStatus.LoggedInAsUser
            )
    }

    @Test
    fun `test already logged in as guest`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300, a1RefreshAsGuest = true)
        tokenRefresher.tokenStore.setToken(TokenScope.A1, makeToken(TokenScope.A1, 120, 0, true, "john").str)
        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()

        testObserver
            .awaitCount(1)
            .assertValues(
                AuthStatus.LoggedInAsGuest
            )
    }

    @Test
    fun `test already logged in as user`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300, a1RefreshAsGuest = false)
        tokenRefresher.tokenStore.setToken(TokenScope.A1, makeToken(TokenScope.A1, 120, 0, false, "john").str)
        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()

        testObserver
            .awaitCount(1)
            .assertValues(
                AuthStatus.LoggedInAsUser
            )
    }

    @Test
    fun `test already logged in as user, but forced logout`() {
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300, a1RefreshAsGuest = false, forcedLogoutOnRefresh = true)
        tokenRefresher.tokenStore.setToken(TokenScope.A1, makeToken(TokenScope.A1, 120, 0, false, "john").str)
        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()

        testObserver
            .awaitCount(1)
            .assertValues(
                AuthStatus.NotLoggedIn
            )
    }

    @Test
    fun `test already logged in as user, a1 validates fine, but later is forced logout`() {
        mockWebServer.dispatcher = TestTokenDispatcher(120, 63, a1RefreshAsGuest = false, forcedLogoutOnRefresh = false)
        tokenRefresher.tokenStore.setToken(TokenScope.A1, makeToken(TokenScope.A1, 120, 0, false, "john").str)
        val currentState = tokenRefresher.tokenStore.loginStatus()
        println(currentState)

        val obs = useCase.execute(Unit)
        val testObserver = obs.test()

        Thread.sleep(100)
        mockWebServer.dispatcher = TestTokenDispatcher(120, 300, a1RefreshAsGuest = false, forcedLogoutOnRefresh = false, forcedLogoutOnAuthorize = true)
        Thread.sleep(100)

        testObserver
            .awaitCount(2)

            .assertValues(
                AuthStatus.LoggedInAsUser,
                AuthStatus.ForcedLogout
            )
    }
}