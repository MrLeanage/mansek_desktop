package com.mandiri.most.core.common.domain

import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.toMostAPIError
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import org.reactivestreams.Publisher

class FlowableErrorTransformer<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.onErrorResumeNext {
            Flowable.error(if (it is APIException) it.toMostAPIError() else it)
        }
    }
}