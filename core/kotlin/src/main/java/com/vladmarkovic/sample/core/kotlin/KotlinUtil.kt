/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.core.kotlin

import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * Convenience extension for function which don't return Unit
 * for when statement needing the common return to be Unit.
 */
@Suppress("UnusedReceiverParameter")
fun <T> T.ignoreReturn() = Unit

@Suppress("UNUSED_PARAMETER", "RedundantNullableReturnType")
fun doNothing(vararg args: Any): Any? = Unit


inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    ascending: Boolean,
    crossinline selector: (T) -> R?
): List<T> =
    if (ascending) sortedBy { selector(it) }
    else sortedByDescending { selector(it) }

inline fun <T: Any, R: Any> Optional<T>?.convertTo(converter: (T) -> R): Optional<R> =
    Optional.ofNullable(this?.getOrNull()?.let { converter(it) })
