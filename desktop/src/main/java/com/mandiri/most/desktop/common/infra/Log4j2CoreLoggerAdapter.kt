package com.mandiri.most.desktop.common.infra

import com.mandiri.most.core.init.CoreLogger
import com.mandiri.most.core.init.LogFunc
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager


class Log4j2CoreLoggerAdapter() : CoreLogger {
    override fun v(msg: String?) {
        LogManager.getLogger().log(Level.TRACE, msg)
    }

    override fun v(f: LogFunc) {
        LogManager.getLogger().log(Level.TRACE, f())
    }

    override fun d(msg: String?) {
        LogManager.getLogger().log(Level.DEBUG, msg)
    }

    override fun d(f: LogFunc) {
        LogManager.getLogger().log(Level.DEBUG, f())
    }

    override fun i(msg: String?) {
        LogManager.getLogger().log(Level.INFO, msg)
    }

    override fun i(f: LogFunc) {
        LogManager.getLogger().log(Level.INFO, f())
    }

    override fun w(msg: String?) {
        LogManager.getLogger().log(Level.WARN, msg)
    }

    override fun w(f: LogFunc) {
        LogManager.getLogger().log(Level.WARN, f())
    }

    override fun e(msg: String?) {
        LogManager.getLogger().log(Level.ERROR, msg)
    }

    override fun e(f: LogFunc) {
        LogManager.getLogger().log(Level.ERROR, f())
    }

    override fun wtf(msg: String?) {
        LogManager.getLogger().log(Level.FATAL, msg)
    }

    override fun wtf(f: LogFunc) {
        LogManager.getLogger().log(Level.FATAL, f())
    }
}