package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    @SerialName("token")
    val token: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirm")
    val passwordConfirm: String
)