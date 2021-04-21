package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.common.network.APIError
import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.ApiInterceptor
import com.mandiri.most.core.common.network.ErrorField
import com.mandiri.most.core.common.utils.LocaleManager
import com.mandiri.most.core.util.FakeLocaleManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class ValidatePasswordResetUseCaseTest {
    private val mockWebServer = MockWebServer()
    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: ValidatePasswordResetUseCase
    private val localeManager: LocaleManager = FakeLocaleManager()

    @Before
    fun setup() {
        mockWebServer.start()
        val httpClient = OkHttpClient
            .Builder()
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
        authRepository = DefaultAuthRepository(authApi)
        useCase = ValidatePasswordResetUseCase(authRepository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test validate reset password success`() {
        val (tokenString, _) = makeToken(TokenScope.User, 60, 0)
        val tokenResponse = makeTokenResponse(TokenScope.User, tokenString)

        val response = MockResponse().setResponseCode(200).setBody(tokenResponse)
        mockWebServer.enqueue(response)

        val result = useCase.execute(
            ValidatePasswordResetUseCase.Params(
                "122321",
                "lala@lala.com",
                "123456"
            )
        ).blockingGet()

        Assert.assertEquals(tokenString, result)
    }

    @Test
    fun `test validate reset password return http 400`() {
        val expected = APIError(code = "GE0000", message = "Something wrong")
        val json = """
            {
              "code": "GE0000",
              "message": "Something wrong"
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(400).setBody(json)
        mockWebServer.enqueue(response)

        try {
            useCase.execute(
                ValidatePasswordResetUseCase.Params(
                    "122321",
                    "lala@lala.com",
                    "123456"
                )
            ).blockingGet()
            Assert.fail("should fail here")
        } catch (e: Exception) {
            Assert.assertEquals(expected, (e.cause as? APIException)?.error)
        }
    }

    @Test
    fun `test validate reset password return http 422`() {
        val expected = APIError(
            code =
            "GE0004",
            message = "Something wrong",
            fields = listOf(
                ErrorField("string", "string")
            )
        )
        val json = """
            {
              "code": "GE0004",
              "message": "Something wrong",
              "fields": [
                {
                  "name": "string",
                  "error": "string"
                }
              ]
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(422).setBody(json)
        mockWebServer.enqueue(response)

        try {
            useCase.execute(
                ValidatePasswordResetUseCase.Params(
                    "122321",
                    "lala@lala.com",
                    "123456"
                )
            ).blockingGet()
            Assert.fail("should fail here")
        } catch (e: Exception) {
            Assert.assertEquals(expected, (e.cause as? APIException)?.error)
        }
    }
}