package com.mandiri.most.core.user.domain.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.common.network.APIError
import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.ApiInterceptor
import com.mandiri.most.core.common.network.genericAPIError
import com.mandiri.most.core.user.data.repository.UserDefaultRepository
import com.mandiri.most.core.user.data.source.remote.UserApi
import com.mandiri.most.core.user.domain.LoginData
import com.mandiri.most.core.user.domain.User
import com.mandiri.most.core.util.FakeLocaleManager
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class UserRepositoryTest {

    private val mockWebServer = MockWebServer()
    private lateinit var repository: UserRepository
    private val localeManager = FakeLocaleManager()

    @Before
    @ExperimentalSerializationApi
    fun setup() {
        mockWebServer.start()
        val httpClient =
            OkHttpClient.Builder().addInterceptor(ApiInterceptor(localeManager)).build()
        val contentType = "application/json".toMediaType()
        val userApi =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(Json.asConverterFactory(contentType))
                .build()
                .create(UserApi::class.java)
        repository = UserDefaultRepository(userApi)
    }

    @Test
    fun `test login successfully`() {

        val expected = User("test", "Jason", "aXs12345")
        val json = """
            {
                "data": {
                    "user_id": "test",
                    "name": "Jason",
                    "password": "aXs12345"
                }
            }
        """.trimIndent()
        val response = MockResponse().setResponseCode(200).setBody(json)
        mockWebServer.enqueue(response)

        val result = repository.login(LoginData(expected.id, expected.password)).blockingGet()

        assertEquals(expected, result)
    }

    @Test
    fun `test unauthorized login`() {
        val expected = APIError(
            code = "ERR-1000",
            "Login credentials invalid"
        )
        val json = """
            {
                "code": "ERR-1000",
                "message": "Login credentials invalid"
            }
        """.trimIndent()
        val response = MockResponse().setResponseCode(410).setBody(json)
        mockWebServer.enqueue(response)

        kotlin.runCatching {
            repository.login(LoginData("test", "password")).blockingGet()
            fail("Should not pass login")
        }.onFailure {
            assertEquals(expected, (it.cause as? APIException)?.error)
        }
    }

    @Test
    fun `test network 404 error`() {
        val response = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(response)

        kotlin.runCatching {
            repository.login(LoginData("test", "password")).blockingGet()
            fail("Should not pass login")
        }.onFailure {
            assertEquals(genericAPIError.code, (it.cause as? APIException)?.error?.code)
        }
    }
}