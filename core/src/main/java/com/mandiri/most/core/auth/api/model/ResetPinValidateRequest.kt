package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPinValidateRequest(
    @SerialName("otp")
    val otp: String
)