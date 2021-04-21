package com.mandiri.most.core.auth.token

import com.mandiri.most.core.common.utils.EnumCompanion
import com.mandiri.most.core.init.NetworkConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.Instant

enum class TokenScope(val value: String) {
    User(NetworkConstants._X_AUTHSCOPE_VAL_SVC_USER),
    Portfolio(NetworkConstants._X_AUTHSCOPE_VAL_SVC_PORTFOLIO),
    Order(NetworkConstants._X_AUTHSCOPE_VAL_SVC_ORDER),
    Market(NetworkConstants._X_AUTHSCOPE_VAL_SVC_MARKET),
    A1(NetworkConstants._X_AUTHSCOPE_VAL_A1TOKEN),
    Pin(NetworkConstants._X_AUTHSCOPE_VAL_PIN);

    companion object : EnumCompanion<String, TokenScope>(TokenScope.values().associateBy(TokenScope::value))
}

@Serializable
sealed class BaseToken {
    abstract val issuer: String
    abstract val subject: String
    abstract val expiration: Long
    abstract val issuance: Long
    abstract val tokenId: String

    fun expirationInstant(): Instant {
        return Instant.ofEpochSecond(expiration)
    }

    fun issuanceInstant(): Instant {
        return Instant.ofEpochSecond(issuance)
    }
}

@Serializable
data class A1Token(
    @SerialName("iss")
    override val issuer: String,

    @SerialName("sub")
    override val subject: String,

    @SerialName("exp")
    override val expiration: Long,

    @SerialName("iat")
    override val issuance: Long,

    @SerialName("tokenid")
    override val tokenId: String,

    @SerialName("guest")
    val guest: Boolean,

    @SerialName("userid")
    val userId: String = ""

): BaseToken()

@Serializable
data class A2Token(
    @SerialName("iss")
    override val issuer: String,

    @SerialName("sub")
    override val subject: String,

    @SerialName("exp")
    override val expiration: Long,

    @SerialName("iat")
    override val issuance: Long,

    @SerialName("guest")
    val guest: Boolean,

    @SerialName("tokenid")
    override val tokenId: String,

    @SerialName("scope")
    val scope: String,

    @SerialName("market.data")
    val delayMinutes: Int = 15

): BaseToken()

@Serializable
data class PinToken(
    @SerialName("iss")
    override val issuer: String,

    @SerialName("sub")
    override val subject: String,

    @SerialName("exp")
    override val expiration: Long,

    @SerialName("iat")
    override val issuance: Long,

    @SerialName("tokenid")
    override val tokenId: String,

    @SerialName("scope")
    val scope: String

): BaseToken()

internal fun BaseToken.isExpired(): Boolean {
    val durationLeft = Duration.between(Instant.now(), this.expirationInstant())
    return durationLeft.isNegative
}

internal fun BaseToken.secondsLeft(): Long {
    val durationLeft = Duration.between(Instant.now(), this.expirationInstant())
    return when(durationLeft.isNegative) {
        true -> 0L
        false -> durationLeft.seconds
    }
}

internal fun BaseToken.secondsSinceIssued(): Long {
    val durationSinceIssued = Duration.between(this.issuanceInstant(), Instant.now())
    return when(durationSinceIssued.isNegative) {
        true -> 0L
        false -> durationSinceIssued.seconds as Long
    }
}
