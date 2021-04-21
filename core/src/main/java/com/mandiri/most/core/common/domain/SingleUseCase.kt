package com.mandiri.most.core.common.domain

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class SingleUseCase<T, P> {

    fun execute(params: T): Single<P> {
        return build(params).compose(SingleErrorTransformer()).subscribeOn(Schedulers.io())
    }

    protected abstract fun build(params: T): Single<P>
}
