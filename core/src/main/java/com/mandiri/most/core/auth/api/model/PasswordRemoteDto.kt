package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    @SerialName("user_id")
    val userId: String,
    @SerialName("email")
    val email: String
)

@Serializable
data class ForgotPasswordResponse(
    @SerialName("expires_at")
    val expiredAt: String
)

@Serializable
data class ResetValidateRequest(
    @SerialName("user_id")
    val userId: String,
    @SerialName("email")
    val email: String,
    @SerialName("otp")
    val otp: String
)