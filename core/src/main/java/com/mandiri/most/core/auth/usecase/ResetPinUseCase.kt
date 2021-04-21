package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.common.domain.SingleUseCase
import io.reactivex.rxjava3.core.Single

class ResetPinUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ResetPinUseCase.Params, Unit>() {

    override fun build(params: Params): Single<Unit> {
        return params.run { authRepository.resetPin(token, pin, pinConfirm) }
    }

    data class Params(
        val token: String,
        val pin: String,
        val pinConfirm: String
    )
}