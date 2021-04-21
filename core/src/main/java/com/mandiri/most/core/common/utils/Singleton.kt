package com.mandiri.most.core.common.utils

open class Singleton<T, in A>(private val constructor: (A) -> T) {

    @Volatile
    var instance: T? = null

    fun initOnce(arg: A): T {
        return when {
            instance != null -> instance!!
            else -> synchronized(this) {
                if (instance == null) instance = constructor(arg)
                instance!!
            }
        }
    }
}

/**
private fun usageDemo() {
    class TestClass(val param: String) {
        fun doSomething() {}
    }

    val s = Singleton{ param: String -> TestClass(param)}
    s.initOnce("aaa")
    s.instance?.doSomething()
}
 **/