package com.mandiri.most.desktop.common

import java.util.concurrent.atomic.AtomicBoolean

class Event<T>(private val data: T) {
    private var isConsumed: AtomicBoolean = AtomicBoolean(false)
    fun consumeOnce(listener: (T) -> Unit) {
        if (isConsumed.compareAndSet(false, true)) {
            listener(data)
        }
    }

    fun peek(): T? = if (!isConsumed.get()) data else null

    fun dispose() {
        isConsumed.set(true)
    }
}