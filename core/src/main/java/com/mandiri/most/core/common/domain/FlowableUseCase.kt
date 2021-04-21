package com.mandiri.most.core.common.domain

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class FlowableUseCase<T, P> {

    fun execute(params: T): Flowable<P> {
        return build(params).compose(FlowableErrorTransformer()).subscribeOn(Schedulers.io())
    }

    protected abstract fun build(params: T): Flowable<P>
}