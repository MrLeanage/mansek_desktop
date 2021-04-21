package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCredentialRequest(
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    val scheme: String,
    val token: String,
    val scope: String,

    @SerialName("expires_at")
    val expiresAt: String
)

@Serializable
data class PinTokenRequest(
    val pin: String,

    @SerialName("expires_in")
    val validityMinutes: Long = 120
)