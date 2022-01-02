package com.vladmarkovic.sample.shared_test

import com.vladmarkovic.sample.shared_domain.log.Logger

class TestLogger : Logger {

    override fun v(t: Throwable) {
        println("${t.stackTrace}")
    }

    override fun v(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun v(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun d(t: Throwable) {
        println("${t.stackTrace}")    }

    override fun d(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun d(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun i(t: Throwable) {
        println("${t.stackTrace}")    }

    override fun i(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun i(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun w(t: Throwable) {
        println("${t.stackTrace}")    }

    override fun w(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun w(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun e(t: Throwable) {
        println("${t.stackTrace}")    }

    override fun e(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun e(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun wtf(t: Throwable) {
        println("${t.stackTrace}")    }

    override fun wtf(t: Throwable, message: String, vararg formatArgs: Any) {
        println("${String.format(message, formatArgs)}, ${t.stackTrace}")    }

    override fun wtf(message: String, vararg formatArgs: Any) {
        println(String.format(message, formatArgs))    }

    override fun tag(tag: String): Logger = this

    override val tag: String = "TEST_TAG"
}
