package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPinRequest(
    @SerialName("token")
    val token: String,
    @SerialName("pin")
    val pin: String,
    @SerialName("pin_confirm")
    val pinConfirm: String
)