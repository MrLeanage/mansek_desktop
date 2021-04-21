package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.model.TokenResponse
import com.mandiri.most.core.auth.token.TokenScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun makeTokenResponse(scope: TokenScope, tokenString: String): String {
    val response = TokenResponse(
        scheme = "Bearer",
        token = tokenString,
        scope = scope.value,
        expiresAt = "2019-08-24T14:15:22Z"
    )
    return Json.encodeToString(response)
}