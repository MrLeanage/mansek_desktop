package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.model.PinTokenRequest
import com.mandiri.most.core.auth.api.model.TokenResponse
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.auth.api.source.network.TokenInjector
import com.mandiri.most.core.auth.token.DefaultTokenStore
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.auth.token.TokenStore
import com.mandiri.most.core.common.network.ApiInterceptor
import com.mandiri.most.core.init.NetworkConstants
import com.mandiri.most.core.util.FakeLocaleManager
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class ValidatePinUseCaseTest {

    private val mockWebServer = MockWebServer()
    private lateinit var tokenStore: TokenStore
    private lateinit var repository: AuthRepository
    private lateinit var useCase: ValidatePinUseCase
    private val localeManager = FakeLocaleManager()

    @Before
    @ExperimentalSerializationApi
    fun setup() {
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
        repository = DefaultAuthRepository(authApi)
        useCase = ValidatePinUseCase(repository)
    }

    @Test
    fun `test no a1token present insertion`() {

        val (tokenString, _) = makeToken(TokenScope.Pin, 60, 0)
        val pinResponse = makeTokenResponse(TokenScope.Pin, tokenString)

        val response = MockResponse().setResponseCode(200).setBody(pinResponse)
        mockWebServer.enqueue(response)

        val result = repository.pinToken(PinTokenRequest("123456", 30)).blockingGet()

        assertEquals(result.token, tokenString)

        val request = mockWebServer.takeRequest()
        assertEquals("/user/token/pin", request.path)
        assertEquals(request.getHeader(NetworkConstants._X_AUTHSCOPE_KEY), "a1token")
        assertEquals(request.getHeader("Authorization"), "Bearer not_found")
    }

    @Test
    fun `test a1token insertion from tokenstore`() {

        val (a1Token, _) = makeToken(TokenScope.A1, 60, 0)
        tokenStore.setToken(TokenScope.A1, a1Token)

        val (tokenString, _) = makeToken(TokenScope.Pin, 60, 0)

        val pinResponse = makeTokenResponse(TokenScope.Pin, tokenString)
        val response = MockResponse().setResponseCode(200).setBody(pinResponse)
        mockWebServer.enqueue(response)

        val result = repository.pinToken(PinTokenRequest("123456", 30)).blockingGet()

        assertEquals(result.token, tokenString)

        val request = mockWebServer.takeRequest()
        assertEquals("/user/token/pin", request.path)
        assertEquals(request.getHeader(NetworkConstants._X_AUTHSCOPE_KEY), "a1token")
        assertEquals(request.getHeader("Authorization"), "Bearer $a1Token")
    }

    private fun makeTokenResponse(scope: TokenScope, tokenString: String): String {
        val response = TokenResponse(
            scheme = "Bearer",
            token = tokenString,
            scope = scope.value,
            expiresAt = "2019-08-24T14:15:22Z"
        )
        return Json.encodeToString(response)
    }
}
