package com.mandiri.most.core.common.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class ErrorResponse(
    @SerialName("error")
    val error: String? = null,
    @SerialName("message")
    val message: String? = null
)

@Serializable
open class RestApiResponse<T>(
    @SerialName("data")
    val data: T? = null
) : ErrorResponse()