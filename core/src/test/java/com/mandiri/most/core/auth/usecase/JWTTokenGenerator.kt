package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.token.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.*
import kotlin.random.Random

internal data class TokenPair(val str: JWTToken, val token: BaseToken)

internal fun makeToken(scope: TokenScope, lifetimeSec: Long, backDateInSec: Long, guest: Boolean = false, userId: String = "t@example.com"): TokenPair {

    val now = Instant.now().epochSecond
    val token: BaseToken
    val tokenId = Random.nextLong()

    val jsonString = when(scope) {
        TokenScope.A1 -> {
            token = A1Token(
                issuer = "AUMS",
                subject = "A1V1",
                expiration = now + lifetimeSec,
                issuance = now - backDateInSec,
                tokenId = "$tokenId",
                userId = userId,
                guest = guest
            )
            Json.encodeToString(token)
        }
        TokenScope.Pin -> {
            token = PinToken(
                issuer = "AUMS",
                subject = "PINV1",
                scope = scope.value,
                expiration = now + lifetimeSec,
                issuance = now - backDateInSec,
                tokenId = "$tokenId"
            )
            Json.encodeToString(token)
        }
        else -> {
            token = A2Token(
                issuer = "AUMS",
                subject = "A2V1",
                scope = scope.value,
                expiration = now + lifetimeSec,
                issuance = now - backDateInSec,
                tokenId = "$tokenId",
                guest = guest
            )
            Json.encodeToString(token)
        }
    }

    val str = Base64.getEncoder().encode(jsonString.toByteArray()).toString(Charsets.UTF_8)
    return TokenPair(str, token)
}