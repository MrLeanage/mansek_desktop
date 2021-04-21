package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.model.PinTokenRequest
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.common.domain.SingleUseCase
import com.mandiri.most.core.common.network.MostAPIException
import com.mandiri.most.core.common.network.toMostAPIError
import com.mandiri.most.core.init.globalTokenStore
import io.reactivex.rxjava3.core.Single

/**
 *  The execution of this use case will return a Single. The result is:
 *  - a MostAPIError if some type of network or processing error occurred
 *  - true if the pin is correct
 *  - false if the pin is incorrect
 */
class ValidatePinUseCase(
    private val authRepository: AuthRepository
): SingleUseCase<PinTokenRequest, Boolean>() {

    override fun build(params: PinTokenRequest): Single<Boolean> {
        return authRepository
            .pinToken(params)
            .flatMap {
                globalTokenStore.instance?.savePin(params.pin)
                val ok = globalTokenStore.instance?.setToken(TokenScope.Pin, it.token) ?: false
                when(ok) {
                    false -> Single.error(MostAPIException.BadToken)
                    else -> Single.just(true)
                }
            }
            .onErrorReturn {
                val mostError = it.toMostAPIError()
                when(mostError) {
                    is MostAPIException.InvalidCredentials -> false
                    else -> throw mostError
                }
            }

    }
}