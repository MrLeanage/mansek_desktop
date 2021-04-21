package com.mandiri.most.core.auth.api

import com.mandiri.most.core.auth.api.model.PinTokenRequest
import com.mandiri.most.core.auth.api.model.RequestExpiration
import com.mandiri.most.core.auth.api.model.TokenResponse
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.auth.usecase.UserCredential
import io.reactivex.rxjava3.core.Single

interface AuthRepository {

    fun doLogin(credential: UserCredential): Single<TokenResponse>

    fun loginAsGuest(): Single<TokenResponse>

    fun authorize(scopes: List<TokenScope>): Single<List<TokenResponse>>

    fun pinToken(request: PinTokenRequest): Single<TokenResponse>

    fun a1Refresh(): Single<TokenResponse>

    fun forgotPassword(userId: String, email: String): Single<RequestExpiration>

    fun resetPassword(token: String, password: String, passwordConfirm: String): Single<Unit>

    fun resetPasswordValidate(userId: String, email: String, otp: String): Single<TokenResponse>

    fun resetPin(token: String, pin: String, pinConfirm: String): Single<Unit>

    fun resetPinValidate(otp: String): Single<TokenResponse>

    fun forgotPin(password: String): Single<RequestExpiration>
}