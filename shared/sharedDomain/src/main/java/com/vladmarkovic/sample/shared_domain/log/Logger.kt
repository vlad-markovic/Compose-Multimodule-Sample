/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_domain.log

/** Abstraction for common logging functions, enabling logging in plain Kotlin modules. */
interface Logger  {

    fun v(t: Throwable)
    fun v(t: Throwable, message: String, vararg formatArgs: Any)
    fun v(message: String, vararg formatArgs: Any)

    fun d(t: Throwable)
    fun d(t: Throwable, message: String, vararg formatArgs: Any)
    fun d(message: String, vararg formatArgs: Any)

    fun i(t: Throwable)
    fun i(t: Throwable, message: String, vararg formatArgs: Any)
    fun i(message: String, vararg formatArgs: Any)

    fun w(t: Throwable)
    fun w(t: Throwable, message: String, vararg formatArgs: Any)
    fun w(message: String, vararg formatArgs: Any)

    fun e(t: Throwable)
    fun e(t: Throwable, message: String, vararg formatArgs: Any)
    fun e(message: String, vararg formatArgs: Any)

    fun wtf(t: Throwable)
    fun wtf(t: Throwable, message: String, vararg formatArgs: Any)
    fun wtf(message: String, vararg formatArgs: Any)

    fun tag(tag: String): Logger

    val tag: String
}
