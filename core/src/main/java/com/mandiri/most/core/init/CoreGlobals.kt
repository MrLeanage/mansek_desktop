package com.mandiri.most.core.init

internal object NetworkConstants {
    // Base Constants related to management of tokens in retrofit API definitions and token injection
    const val _X_AUTHSCOPE_KEY = "X-Authorization-Scope"
    const val _X_AUTHSCOPE_VAL_A1TOKEN = "a1token"
    const val _X_AUTHSCOPE_VAL_SVC_USER = "user"
    const val _X_AUTHSCOPE_VAL_SVC_PORTFOLIO = "portfolio"
    const val _X_AUTHSCOPE_VAL_SVC_ORDER = "order"
    const val _X_AUTHSCOPE_VAL_SVC_MARKET = "market"
    const val _X_AUTHSCOPE_VAL_PIN = "pin"

    // Use these constants in Retrofit API definitions using @Headers()
    const val X_AUTHSCOPE_A1TOKEN = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_A1TOKEN"
    const val X_AUTHSCOPE_SVC_USER = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_SVC_USER"
    const val X_AUTHSCOPE_SVC_PORTFOLIO = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_SVC_PORTFOLIO"
    const val X_AUTHSCOPE_SCV_ORDER = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_SVC_ORDER"
    const val X_AUTHSCOPE_SVC_MARKET = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_SVC_MARKET"
    const val X_AUTHSCOPE_PIN = "$_X_AUTHSCOPE_KEY: $_X_AUTHSCOPE_VAL_PIN"

    // Default request timeout
    const val DEFAULT_TIMEOUT_IN_SECONDS = 30L

    // Delay between A1 refresh attempts
    const val A1_REFRESH_MIN_DELAY_SEC = 300L

    // Max time since A1 last refresh
    const val A1_REFRESH_MAX_WAIT_SEC = 3600L

    // Force A1 refresh if time left
    const val A1_FORCE_REFRESH_WHEN_SEC_LEFT = 86_400L

    // Force A2 Refresh if time left
    const val A2_FORCE_REFRESH_WHEN_SEC_LEFT = 60L
}