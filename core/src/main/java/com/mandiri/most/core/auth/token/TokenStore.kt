package com.mandiri.most.core.auth.token

import com.mandiri.most.core.auth.api.model.AuthenticationType
import java.time.Instant

interface TokenStore {

    fun initialize()

    fun setToken(scope: TokenScope, jwtToken: JWTToken): Boolean

    fun getTokenString(scope: TokenScope): String?

    fun getTokenObject(scope: TokenScope): BaseToken?

    fun getTokenStringByXAuth(value: String): String?

    fun loginStatus(): AuthenticationType

    fun deleteAllTokens()

    fun getTokensTimeInfo(): List<TokenTimeInfo>

    fun savePin(pin: String)

    fun getPin(): String?
}

data class TokenTimeInfo(
    val scope: TokenScope,
    val issuance: Instant,
    val expiration: Instant,
    val isExpired: Boolean,
    val secondsSinceIssue: Long,
    val secondsUntilExpiry: Long
)