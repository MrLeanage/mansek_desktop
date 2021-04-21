package com.mandiri.most.core.common.domain

import com.mandiri.most.core.common.network.APIException
import com.mandiri.most.core.common.network.toMostAPIError
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.core.SingleTransformer

class SingleErrorTransformer<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
            .onErrorResumeNext { Single.error(if (it is APIException) it.toMostAPIError() else it) }
    }
}