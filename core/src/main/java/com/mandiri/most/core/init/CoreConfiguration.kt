package com.mandiri.most.core.init

import com.mandiri.most.core.auth.management.SecureKeyValueStore
import com.mandiri.most.core.common.utils.LocaleManager
import okhttp3.Interceptor

data class CoreConfiguration(
    val enableDebug: Boolean = false,
    val baseUrl: String,
    val secureStore: SecureKeyValueStore,
    val platformInterceptor: Interceptor?,
    val localeManager: LocaleManager,
    val logger: CoreLogger?
)