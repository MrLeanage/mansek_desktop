package com.mandiri.most.core.common.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *
 * Given an input of type I (=Input), this use case return an observable of type O (= Output)
 *
 * Once the observable is established, the caller may want to interact at some later point with the use case,
 * such as doing certain operations, or maybe when done, doing some type of close operation.
 *
 * To facilitate this, a send function can be provided. It takes a parameter of type A (= Action).
 * U itself can be an enum or sealed class in order to facilitate multiple types of actions
 */
abstract class ObservableUseCase<I, A, O, R> {

    fun execute(params: I): Observable<O> {
        return build(params).subscribeOn(Schedulers.io())
    }

    abstract fun send(params: A): R

    protected abstract fun build(params: I): Observable<O>
}