package com.mandiri.most.core.auth.api.source.network

import com.mandiri.most.core.auth.api.model.*
import com.mandiri.most.core.init.NetworkConstants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


internal interface AuthAPI {

    @POST("/user/login")
    fun login(@Body credentialRequest: UserCredentialRequest): Single<TokenResponse>

    @POST("/user/login/guest")
    fun loginAsGuest(): Single<TokenResponse>

    @Headers(NetworkConstants.X_AUTHSCOPE_A1TOKEN)
    @POST("/user/token/authorize")
    fun authorize(@Body scopes: TokenAuthorizationRequest): Single<List<TokenResponse>>

    @Headers(NetworkConstants.X_AUTHSCOPE_A1TOKEN)
    @POST("/user/token/pin")
    fun pinToken(@Body request: PinTokenRequest): Single<TokenResponse>

    @Headers(NetworkConstants.X_AUTHSCOPE_A1TOKEN)
    @POST("/user/token/refresh")
    fun a1Refresh(): Single<TokenResponse>

    @POST("/user/password/forgot")
    @Headers("Content-Type: application/json")
    fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Single<ForgotPasswordResponse>

    @POST("/user/password/reset")
    @Headers("Content-Type: application/json")
    fun resetPassword(@Body request: ResetPasswordRequest): Single<Unit>

    @POST("/user/password/reset-validate")
    @Headers("Content-Type: application/json")
    fun resetPasswordValidate(@Body request: ResetValidateRequest): Single<TokenResponse>

    @POST("/user/pin/reset")
    @Headers(NetworkConstants.X_AUTHSCOPE_SVC_USER, "Content-Type: application/json")
    fun resetPin(@Body request: ResetPinRequest): Single<Unit>

    @POST("/user/pin/reset-validate")
    @Headers(NetworkConstants.X_AUTHSCOPE_SVC_USER, "Content-Type: application/json")
    fun resetPinValidate(@Body request: ResetPinValidateRequest): Single<TokenResponse>

    @POST("/user/pin/forgot")
    @Headers(NetworkConstants.X_AUTHSCOPE_SVC_USER, "Content-Type: application/json")
    fun forgotPin(@Body request: ForgotPinRequest): Single<ForgotPinResponse>
}

