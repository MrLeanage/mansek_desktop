package com.mandiri.most.core.auth.api.source.network

import com.mandiri.most.core.auth.token.TokenStore
import com.mandiri.most.core.init.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response

internal class TokenInjector(private val provider: TokenStore): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authScope = request.headers[NetworkConstants._X_AUTHSCOPE_KEY]

        authScope?.let {
            val tokenString = provider.getTokenStringByXAuth(it) ?: "not_found"
            val newRequest = request
                    .newBuilder()
                    .header("Authorization", "Bearer $tokenString")
                    .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(request)
    }
}