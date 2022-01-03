/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

val Context.asActivity: Activity
    get() = this.let {
        var context = it
        while (context is ContextWrapper) {
            if (context is Activity) return@let context
            context = context.baseContext
        }
        throw IllegalStateException("Expected an activity context but instead found: $context")
    }

