package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.common.domain.SingleUseCase
import io.reactivex.rxjava3.core.Single

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ResetPasswordUseCase.Params, Unit>() {

    override fun build(params: Params): Single<Unit> {
        return params.run { authRepository.resetPassword(token, password, passwordConfirm) }
    }

    data class Params(
        val token: String,
        val password: String,
        val passwordConfirm: String
    )
}