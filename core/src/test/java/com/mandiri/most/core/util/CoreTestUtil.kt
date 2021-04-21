package com.mandiri.most.core.util

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mandiri.most.core.common.network.ApiInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

fun Retrofit.Builder.defaultSetup(mockWebServer: MockWebServer): Retrofit.Builder {
    val localeManager = FakeLocaleManager()
    val httpClient =
        OkHttpClient.Builder().addInterceptor(ApiInterceptor(localeManager)).build()
    val contentType = "application/json".toMediaType()
    return baseUrl(mockWebServer.url("/"))
        .client(httpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(Json.asConverterFactory(contentType))
}