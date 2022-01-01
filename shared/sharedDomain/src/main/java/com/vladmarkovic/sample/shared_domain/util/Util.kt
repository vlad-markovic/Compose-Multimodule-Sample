package com.vladmarkovic.sample.shared_domain.util

/**
 * Convenience extension for function which don't return Unit
 * for when statement needing the common return to be Unit.
 */
@Suppress("unused")
fun <T> T.ignoreReturn() = Unit
