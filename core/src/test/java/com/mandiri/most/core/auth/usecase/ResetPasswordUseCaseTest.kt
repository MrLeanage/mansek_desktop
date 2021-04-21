package com.mandiri.most.core.auth.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.source.network.AuthAPI
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

class ResetPasswordUseCaseTest {
    private val mockWebServer = MockWebServer()
    private lateinit var useCase: ResetPasswordUseCase
    private val localeManager = FakeLocaleManager()

    @Before
    fun setup() {
        mockWebServer.start()
        val httpClient =
            OkHttpClient.Builder().addInterceptor(ApiInterceptor(localeManager)).build()
        val contentType = "application/json".toMediaType()
        val api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(httpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(AuthAPI::class.java)
        val authRepository = DefaultAuthRepository(api)
        useCase = ResetPasswordUseCase(authRepository)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test reset password no error`() {
        val expected = Unit
        val response = MockResponse().setResponseCode(200).setBody("")
        mockWebServer.enqueue(response)

        val result = useCase.execute(
            ResetPasswordUseCase.Params("1234", "password", "password")
        ).blockingGet()
        Assert.assertEquals(expected, result)
    }
}