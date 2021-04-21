package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.model.RequestExpiration
import com.mandiri.most.core.common.domain.SingleUseCase
import io.reactivex.rxjava3.core.Single

class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ForgotPasswordUseCase.Params, RequestExpiration>() {

    override fun build(params: Params): Single<RequestExpiration> {
        return params.run { authRepository.forgotPassword(userId, email) }
    }

    data class Params(
        val userId: String,
        val email: String
    )
}