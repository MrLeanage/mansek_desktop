package com.mandiri.most.core.common.network

import com.mandiri.most.core.common.utils.LocaleManager
import com.mandiri.most.core.init.log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor(private val localeManager: LocaleManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", localeManager.appLanguage.value)
            .build()
        val response = chain.proceed(request)
        if (!response.isSuccessful) {
            try {
                val errorBody = response.body?.string()
                val apiError = errorBody?.let {
                    Json.decodeFromString<APIError>(it)
                }
                log.d("API Error: " + apiError.toString())
                apiError?.status = response.code

                throw APIException(apiError ?: genericAPIError, "api error")
            } catch (e: APIException) {
                throw e
            } catch (e: Exception) {
                throw APIException(genericAPIError, e.message)
            }
        }
        return response
    }
}