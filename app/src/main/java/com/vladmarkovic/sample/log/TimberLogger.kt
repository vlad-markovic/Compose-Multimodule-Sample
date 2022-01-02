package com.vladmarkovic.sample.log

import android.os.Build
import com.vladmarkovic.sample.shared_domain.log.Logger
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

/** [Logger] implementation using [Timber]. */
@Singleton
class TimberLogger @Inject constructor() : Logger {

    // region tag
    private companion object {
        private const val MAX_TAG_LENGTH: Int = 23
        private const val CALL_STACK_INDEX: Int = 4
        private val ANONYMOUS_CLASS: Pattern = Pattern.compile("(\\$\\d+)+$")
    }

    override var tag: String = ""
        private set
        get() = if (field.isNotBlank()) {
            val tag = field
            field = ""
            tag
        } else {
            stackElementTag
        }

    // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
    // because Robolectric runs them on the JVM but on Android the elements are different.
    private val stackElementTag: String
        get() =
            try {
                val stackTrace = Throwable().stackTrace
                check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
                val element = stackTrace[CALL_STACK_INDEX]
                var className = element.className
                val matcher: Matcher = ANONYMOUS_CLASS.matcher(className)
                if (matcher.find()) {
                    className = matcher.replaceAll("")
                }
                className = className.substring(className.lastIndexOf('.') + 1)
                // Tag length limit was removed in API 24.
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    val tag = "$className.${element.methodName}.${element.lineNumber}"
                    val isInClass = className.commonSuffixWith(element.fileName, true).isEmpty()
                    val classAndFileNamesAreDifferent = isInClass &&
                            element.fileName.substring(
                                0,
                                element.fileName.lastIndexOf('.')
                            ) != className
                    if (isInClass && classAndFileNamesAreDifferent) {
                        "${element.fileName}[$tag]"
                    } else {
                        tag
                    }
                } else {
                    className.substring(0, min(className.length, MAX_TAG_LENGTH))
                }
            } catch (e: Exception) {
                when (e) {
                    is ClassCastException, is IndexOutOfBoundsException -> e.message
                        ?: e.stackTraceToString()
                    else -> throw e
                }
            }

    @Suppress("unused")
    private fun <T> T.resetTag() {
        tag = ""
    }

    override fun tag(tag: String): Logger {
        this.tag = tag
        return this
    }
    // endregion tag

    override fun v(t: Throwable) {
        Timber.tag(tag).v(t).resetTag()
    }

    override fun v(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).v(t, message, formatArgs).resetTag()
    }

    override fun v(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).v(message, formatArgs).resetTag()
    }


    override fun d(t: Throwable) {
        Timber.tag(tag).d(t).resetTag()
    }

    override fun d(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).d(t, message, formatArgs)
    }

    override fun d(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).d(message, formatArgs).resetTag()
    }


    override fun i(t: Throwable) {
        Timber.tag(tag).i(t).resetTag()
    }

    override fun i(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).i(t, message, formatArgs).resetTag()
    }

    override fun i(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).i(message, formatArgs).resetTag()
    }


    override fun w(t: Throwable) {
        Timber.tag(tag).w(t).resetTag()
    }

    override fun w(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).w(t, message, formatArgs).resetTag()
    }

    override fun w(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).w(message, formatArgs).resetTag()
    }


    override fun e(t: Throwable) {
        Timber.tag(tag).e(t).resetTag()
    }

    override fun e(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).e(t, message, formatArgs).resetTag()
    }

    override fun e(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).e(message, formatArgs).resetTag()
    }


    override fun wtf(t: Throwable) {
        Timber.tag(tag).wtf(t).resetTag()
    }

    override fun wtf(t: Throwable, message: String, vararg formatArgs: Any) {
        Timber.tag(tag).wtf(t, message, formatArgs).resetTag()
    }

    override fun wtf(message: String, vararg formatArgs: Any) {
        Timber.tag(tag).wtf(message, formatArgs).resetTag()
    }
}
