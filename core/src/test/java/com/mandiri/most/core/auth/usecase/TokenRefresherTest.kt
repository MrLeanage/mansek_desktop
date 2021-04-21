package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.model.TokenResponse
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class TokenRefresherTest {

    private val mockWebServer = MockWebServer()
    private lateinit var tokenRefresher: TokenRefresher
    private lateinit var tokenStore: TokenStore
    private lateinit var repository: AuthRepository

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
            .addInterceptor(ApiInterceptor(FakeLocaleManager()))
            .build()

        val authApi =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(AuthAPI::class.java)
        repository = DefaultAuthRepository(authApi)

        val tokenRefresherConfiguration = TokenRefresherConfiguration(
            a1ForceRefreshWhenSecLeft = 30,
            a1RefreshMaxWaitSec = 5,
            a1RefreshMinDelaySec = 3,
            startAfterDelaySec = 1,
            loopEverySec = 1
        )
        tokenRefresher = TokenRefresher(tokenStore, repository, tokenRefresherConfiguration)
    }

    @Test
    fun `test a1token refresh due to min delay`() {
        val (a1Token, token1) = makeToken(TokenScope.A1, 60, 0)
        tokenStore.setToken(TokenScope.A1, a1Token)
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300)

        tokenRefresher.startRefreshing()
        Thread.sleep(7000)
        tokenRefresher.stopRefreshing()
        val storeToken = tokenStore.getTokenObject(TokenScope.A1)
        Assert.assertNotEquals(token1.tokenId, storeToken?.tokenId)
        //Assert.assertEquals(token2.tokenId, storeToken?.tokenId)
    }

    @Test
    fun `test a1token refresh due to nearing expiration`() {
        val (a1Token, token1) = makeToken(TokenScope.A1, 4, 0)
        tokenStore.setToken(TokenScope.A1, a1Token)
        mockWebServer.dispatcher = TestTokenDispatcher(60, 300)
        tokenRefresher.startRefreshing()
        Thread.sleep(4000)
        tokenRefresher.stopRefreshing()
        val storeToken = tokenStore.getTokenObject(TokenScope.A1)
        Assert.assertNotEquals(token1.tokenId, storeToken?.tokenId)
    }

    @Test
    fun `test a2token refresh due to nearing expiration`() {
        val (a1Token, _) = makeToken(TokenScope.A1, 300, 0)
        tokenStore.setToken(TokenScope.A1, a1Token)
        val (a2Token, token2) = makeToken(TokenScope.Market, 45, 0)
        tokenStore.setToken(TokenScope.Market, a2Token)
        mockWebServer.dispatcher = TestTokenDispatcher(300, 300)
        tokenRefresher.startRefreshing()
        Thread.sleep(4000)
        tokenRefresher.stopRefreshing()
        val storeToken = tokenStore.getTokenObject(TokenScope.Market)
        Assert.assertNotEquals(token2.tokenId, storeToken?.tokenId)
    }
}

