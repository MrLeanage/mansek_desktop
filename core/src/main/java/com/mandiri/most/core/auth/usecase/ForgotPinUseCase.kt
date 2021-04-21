package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.model.RequestExpiration
import com.mandiri.most.core.common.domain.SingleUseCase
import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.toMostAPIError
import io.reactivex.rxjava3.core.Single

class ForgotPinUseCase(
    private val authRepository: AuthRepository
) : SingleUseCase<ForgotPinUseCase.Params, RequestExpiration>() {

    override fun build(params: Params): Single<RequestExpiration> {
        return authRepository.forgotPin(params.password)
            .onErrorResumeNext {
                Single.error((it as APIException).toMostAPIError())
            }
    }

    data class Params(val password: String)
}