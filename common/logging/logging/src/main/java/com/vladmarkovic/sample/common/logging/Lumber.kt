/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.logging

import com.vladmarkovic.sample.common.di.model.EntryPointAccessor

/** Logger wrapper object, enabling static logging, as [Lumber.v]. */
object Lumber : Logger {

    private val logger: Logger by lazy {
        EntryPointAccessor.fromApplication(LoggerEntryPoint::class.java).logger()
    }

    override fun v(t: Throwable) {
        logger.tag(tag).v(t)
    }

    override fun v(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).v(t, message, formatArgs)
    }

    override fun v(message: String, vararg formatArgs: Any) {
        logger.tag(tag).v(message, formatArgs)
    }


    override fun d(t: Throwable) {
        logger.tag(tag).d(t)
    }

    override fun d(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).d(t, message, formatArgs)
    }

    override fun d(message: String, vararg formatArgs: Any) {
        logger.tag(tag).d(message, formatArgs)
    }


    override fun i(t: Throwable) {
        logger.tag(tag).i(t)
    }

    override fun i(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).i(t, message, formatArgs)
    }

    override fun i(message: String, vararg formatArgs: Any) {
        logger.tag(tag).i(message, formatArgs)
    }


    override fun w(t: Throwable) {
        logger.tag(tag).w(t)
    }

    override fun w(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).w(t, message, formatArgs)
    }

    override fun w(message: String, vararg formatArgs: Any) {
        logger.tag(tag).w(message, formatArgs)
    }


    override fun e(t: Throwable) {
        logger.tag(tag).e(t)
    }

    override fun e(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).e(t, message, formatArgs)
    }

    override fun e(message: String, vararg formatArgs: Any) {
        logger.tag(tag).e(message, formatArgs)
    }


    override fun wtf(t: Throwable) {
        logger.tag(tag).wtf(t)
    }

    override fun wtf(t: Throwable, message: String, vararg formatArgs: Any) {
        logger.tag(tag).wtf(t, message, formatArgs)
    }

    override fun wtf(message: String, vararg formatArgs: Any) {
        logger.tag(tag).wtf(message, formatArgs)
    }

    override fun tag(tag: String): Logger {
        Lumber.tag = tag
        return this
    }

    override var tag: String = ""
        private set
        get() = logger.tag
}
