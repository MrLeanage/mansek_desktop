package com.mandiri.most.core.init

typealias LogFunc = () -> String?

interface CoreLogger {

    /**
     * For each log level, two functions are provided:
     *  - log with a nullable string; kotlin interpolation can be used
     *  - log with a lambda that may produce a message
     *
     *  Please use lambdas for expensive logging operations.
     *  The lambda will only get evaluated if the log level requires it and short-circuited otherwise
     *
     *  WTF: fatal, execution should stop
     *  Error: log and continue
     *  Warning: may or may not be an issue, log and continue
     *  Informational: general information on what the system is doing
     *  Debug: to be used during normal development cycle
     *  Verbose: to be used solely for debugging activities
     *
     */

    fun v(msg: String?)
    fun v(f: LogFunc)

    fun d(msg: String?)
    fun d(f: LogFunc)

    fun i(msg: String?)
    fun i(f: LogFunc)

    fun w(msg: String?)
    fun w(f: LogFunc)

    fun e(msg: String?)
    fun e(f: LogFunc)

    fun wtf(msg: String?)
    fun wtf(f: LogFunc)
}

internal class NullLogger: CoreLogger {

    override fun v(msg: String?) {
        return
    }

    override fun v(f: LogFunc) {
        return
    }

    override fun d(msg: String?) {
        return
    }

    override fun d(f: LogFunc) {
        return
    }

    override fun i(msg: String?) {
        return
    }

    override fun i(f: LogFunc) {
        return
    }

    override fun w(msg: String?) {
        return
    }

    override fun w(f: LogFunc) {
        return
    }

    override fun e(msg: String?) {
        return
    }

    override fun e(f: LogFunc) {
        return
    }

    override fun wtf(msg: String?) {
        return
    }

    override fun wtf(f: LogFunc) {
        return
    }
}

internal class StdOutAllLogger: CoreLogger {

    override fun v(msg: String?) {
        println(msg)
    }

    override fun v(f: LogFunc) {
        println(f())
    }

    override fun d(msg: String?) {
        println(msg)
    }

    override fun d(f: LogFunc) {
        println(f())
    }

    override fun i(msg: String?) {
        println(msg)
    }

    override fun i(f: LogFunc) {
        println(f())
    }

    override fun w(msg: String?) {
        println(msg)
    }

    override fun w(f: LogFunc) {
        println(f())
    }

    override fun e(msg: String?) {
        println(msg)
    }

    override fun e(f: LogFunc) {
        println(f())
    }

    override fun wtf(msg: String?) {
        println(msg)
    }

    override fun wtf(f: LogFunc) {
        println(f())
    }
}
