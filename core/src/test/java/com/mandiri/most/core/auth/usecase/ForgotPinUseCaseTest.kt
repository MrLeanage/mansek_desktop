package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.model.RequestExpiration
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.common.extension.date.DateFormat
import com.mandiri.most.core.common.extension.date.toDate
import com.mandiri.most.core.common.network.MostAPIException
import com.mandiri.most.core.util.defaultSetup
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ForgotPinUseCaseTest {

    private val mockWebServer = MockWebServer()
    private lateinit var useCase: ForgotPinUseCase

    @Before
    fun setUp() {
        mockWebServer.start()
        val api = Retrofit.Builder().defaultSetup(mockWebServer).build().create(AuthAPI::class.java)
        useCase = ForgotPinUseCase(DefaultAuthRepository(api))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test forgot pin success`() {
        val expected =
            RequestExpiration("2019-08-24T14:15:22Z".toDate(DateFormat.TIME_STAMP_FORMAT))
        val json = """
            {
              "expires_at": "2019-08-24T14:15:22Z"
            }
        """.trimIndent()
        val response = MockResponse().setResponseCode(200).setBody(json)
        mockWebServer.enqueue(response)

        val result = useCase.execute(ForgotPinUseCase.Params("password")).blockingGet()

        assertEquals(expected, result)
    }


    @Test
    fun `test wrong password`() {
        val expected = MostAPIException.WrongPassword
        val json = """
            {
              "code": "E40102",
              "message": "Something wrong"
            }
        """.trimIndent()
        val response = MockResponse().setResponseCode(401).setBody(json)
        mockWebServer.enqueue(response)

        try {
            useCase.execute(ForgotPinUseCase.Params("password")).blockingGet()
            fail("Should not pass")
        } catch (e: Exception) {
            assertEquals(expected, e.cause)
        }
    }
}