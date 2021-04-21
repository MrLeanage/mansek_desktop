package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.model.RequestExpiration
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.common.extension.date.DateFormat
import com.mandiri.most.core.common.extension.date.toDate
import com.mandiri.most.core.common.network.APIError
import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.ApiInterceptor
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

class ForgotPasswordUseCaseTest {
    private val mockWebServer = MockWebServer()
    private lateinit var useCase: ForgotPasswordUseCase
    private val localeManager = FakeLocaleManager()

    @Before
    fun setup() {
        mockWebServer.start()
        val httpClient =
            OkHttpClient.Builder().addInterceptor(ApiInterceptor(localeManager)).build()
        val contentType = "application/json".toMediaType()
        val api =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(Json.asConverterFactory(contentType))
                .build()
                .create(AuthAPI::class.java)
        val authRepository = DefaultAuthRepository(api)
        useCase = ForgotPasswordUseCase(authRepository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test forgot password no error`() {
        val expected =
            RequestExpiration("2019-08-24T14:15:22Z".toDate(DateFormat.TIME_STAMP_FORMAT))

        val json = """
            {
            "expired_at": "2019-08-24T14:15:22Z"
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(200).setBody(json)
        mockWebServer.enqueue(response)

        val result = useCase.execute(
            ForgotPasswordUseCase.Params("1234", "lala@lala.com")
        ).blockingGet()
        Assert.assertEquals(expected, result)
    }


    @Test
    fun `test forgot password http error 400`() {
        val expected =
            APIError("GE0000", "Something wrong")

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
                ForgotPasswordUseCase.Params("1234", "lala@lala.com")
            ).blockingGet()
            Assert.fail("should not pass")
        } catch (e: Exception) {
            Assert.assertEquals(expected, (e.cause as? APIException)?.error)
        }
    }


    @Test
    fun `test forgot password http error 422`() {
        val expected =
            APIError("GE0004", "Something wrong")

        val json = """
            {
              "code": "GE0004",
              "message": "Something wrong"
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(422).setBody(json)
        mockWebServer.enqueue(response)

        try {
            useCase.execute(
                ForgotPasswordUseCase.Params("1234", "lala@lala.com")
            ).blockingGet()
            Assert.fail("should not pass")
        } catch (e: Exception) {
            Assert.assertEquals(expected, (e.cause as? APIException)?.error)
        }
    }


    @Test
    fun `test forgot password http error 429`() {
        val expected =
            APIError("DS0000", "Something wrong")

        val json = """
            {
              "code": "DS0000",
              "message": "Something wrong"
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(429).setBody(json)
        mockWebServer.enqueue(response)

        try {
            useCase.execute(
                ForgotPasswordUseCase.Params("1234", "lala@lala.com")
            ).blockingGet()
            Assert.fail("should not pass")
        } catch (e: Exception) {
            Assert.assertEquals(expected, (e.cause as? APIException)?.error)
        }
    }
}