package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.common.domain.SingleUseCase
import com.mandiri.most.core.init.globalTokenStore
import io.reactivex.rxjava3.core.Single

class GetStoredPinUseCase : SingleUseCase<Unit, String>() {

    override fun build(params: Unit): Single<String> {
        return globalTokenStore.instance?.getPin()?.let {
            Single.just(it)
        } ?: Single.error(NullPointerException("No pin in the storage"))
    }
}