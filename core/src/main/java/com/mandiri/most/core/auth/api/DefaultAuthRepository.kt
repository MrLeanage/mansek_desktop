package com.mandiri.most.core.auth.api

import com.mandiri.most.core.auth.api.model.*
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.auth.usecase.UserCredential
import io.reactivex.rxjava3.core.Single

internal class DefaultAuthRepository(private val authAPI: AuthAPI) : AuthRepository {

    override fun doLogin(credential: UserCredential): Single<TokenResponse> {
        return authAPI.login(credential.toUserCredentialRequest())
    }

    override fun loginAsGuest(): Single<TokenResponse> {
        return authAPI.loginAsGuest()
    }

    override fun authorize(scope: List<TokenScope>): Single<List<TokenResponse>> {
        val scopeStrings = scope.map { it.value }
        return authAPI.authorize(TokenAuthorizationRequest(scopeStrings))
    }

    override fun pinToken(request: PinTokenRequest): Single<TokenResponse> {
        return authAPI.pinToken(request)
    }

    override fun a1Refresh(): Single<TokenResponse> {
        return authAPI.a1Refresh()
    }

    override fun forgotPassword(
        userId: String,
        email: String
    ): Single<RequestExpiration> {
        return authAPI.forgotPassword(
            request = ForgotPasswordRequest(userId, email)
        ).map { it.asRequestExpiration() }
    }

    override fun resetPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): Single<Unit> {
        return authAPI.resetPassword(
            request = ResetPasswordRequest(token, password, passwordConfirm)
        )
    }

    override fun resetPasswordValidate(userId: String, email: String, otp: String): Single<TokenResponse> {
        return authAPI.resetPasswordValidate(
            request = ResetValidateRequest(userId, email, otp)
        )
    }

    override fun resetPin(token: String, pin: String, pinConfirm: String): Single<Unit> {
        return authAPI.resetPin(
            request = ResetPinRequest(token, pin, pinConfirm)
        )
    }

    override fun resetPinValidate(otp: String): Single<TokenResponse> {
        return authAPI.resetPinValidate(
            request = ResetPinValidateRequest(otp)
        )
    }

    override fun forgotPin(password: String): Single<RequestExpiration> {
        return authAPI.forgotPin(
            request = ForgotPinRequest(password)
        ).map { it.asRequestExpiration() }
    }
}