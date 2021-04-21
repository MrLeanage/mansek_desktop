package com.mandiri.most.core.common.network

import kotlinx.serialization.Serializable

@Serializable
data class ErrorField(val name: String, val error: String)

@Serializable
data class APIError(
    val code: String,
    val message: String,
    val fields: List<ErrorField>? = null
) {
    var status: Int = 0
}

var genericAPIError = APIError("0000", "generic error")

enum class ErrorCodes(val value: String) {
    Unspecified("GE0000"),
    InvalidBody("GE0001"),
    Unauthorized("GE0002"),
    DataNotFound("GE0003"),
    BadInput("GE0004"),    //maps to a status code of 422
    ForcedLogout("E40100"),
    InvalidPin("E40101"),
    WrongPassword("E40102"),
    WrongOtp("EOTP001"),
    OtpNotFound("EOTP002")
}

enum class StatusCodes(val value: Int) {
    OK(200),
    Unauthorized(401),
    URLNotFound(404),
    BadInput(422),
    TooManyRequests(429)
}
