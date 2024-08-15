/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.core.android

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

val Context.asActivity: Activity get() = asActivity<Activity>()

inline fun <reified T: Activity> Context.asActivity(): T = this.let {
    var context = it
    while (context is ContextWrapper) {
        if (context is T) return@let context
        context = context.baseContext
    }
    throw IllegalStateException("Expected an activity context but instead found: $context")
}
