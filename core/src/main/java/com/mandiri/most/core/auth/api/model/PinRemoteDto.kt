package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPinRequest(@SerialName("password") val password: String)

@Serializable
data class ForgotPinResponse(@SerialName("expires_at") val expiresAt: String)