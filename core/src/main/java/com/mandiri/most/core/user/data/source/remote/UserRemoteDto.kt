package com.mandiri.most.core.user.data.source.remote

import com.mandiri.most.core.common.network.RestApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias UserResponse = RestApiResponse<UserResponseData>

@Serializable
data class UserResponseData(
    @SerialName("user_id")
    val userId: String,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class LoginRequest(
    @SerialName("user_id")
    val userId: String,
    @SerialName("password")
    val password: String
)