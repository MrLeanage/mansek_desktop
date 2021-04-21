package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.common.domain.SingleUseCase
import io.reactivex.rxjava3.core.Single

class ValidatePinResetUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ValidatePinResetUseCase.Params, String>() {
    override fun build(params: Params): Single<String> {
        return params.run {
            authRepository.resetPinValidate(otp).map { it.token }
        }
    }

    data class Params(val otp: String)
}