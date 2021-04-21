package com.mandiri.most.core.init

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.auth.api.source.network.TokenInjector
import com.mandiri.most.core.auth.management.TokenRefresher
import com.mandiri.most.core.auth.token.TokenStore
import com.mandiri.most.core.common.network.ApiInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit

internal fun makeNetworkModule(config: CoreConfiguration, tokenStore: TokenStore): Module {

    val loggingInterceptor = makeLoggingInterceptor(config.enableDebug)
    val tokenInjector = TokenInjector(tokenStore)
    val apiInterceptor = ApiInterceptor(config.localeManager)
    val client = makeOkHttpClient(
        tokenInjector,
        loggingInterceptor,
        apiInterceptor,
        config.platformInterceptor
    )

    return module {
        single { makeRetrofit(config.baseUrl, client) }
        single { TokenRefresher(tokenStore, get()) }
    }
}

private fun makeLoggingInterceptor(debug: Boolean): HttpLoggingInterceptor {

    return HttpLoggingInterceptor().apply {
        level = if (debug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

private fun makeOkHttpClient(
    tokenInjector: TokenInjector,
    loggingInterceptor: HttpLoggingInterceptor,
    apiInterceptor: ApiInterceptor,
    platformInterceptor: Interceptor?
): OkHttpClient {

    val builder = OkHttpClient.Builder()
        .readTimeout(NetworkConstants.DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .connectTimeout(NetworkConstants.DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NetworkConstants.DEFAULT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(tokenInjector)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiInterceptor)

    platformInterceptor?.let {
        builder.addInterceptor(platformInterceptor)
    }

    return builder.build()
}

private fun makeRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {

    val converterFactory = Json.asConverterFactory("application/json".toMediaType())

    return Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

}