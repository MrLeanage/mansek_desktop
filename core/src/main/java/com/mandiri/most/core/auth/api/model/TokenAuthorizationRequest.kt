package com.mandiri.most.core.auth.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenAuthorizationRequest(val scopes: List<String>)