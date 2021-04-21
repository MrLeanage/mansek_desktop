package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.common.domain.SingleUseCase
import io.reactivex.rxjava3.core.Single

class ValidatePasswordResetUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ValidatePasswordResetUseCase.Params, String>() {
    override fun build(params: Params): Single<String> {
        return params.run {
            authRepository.resetPasswordValidate(userId, email, otp).map { it.token }
        }
    }

    data class Params(val userId: String, val email: String, val otp: String)
}