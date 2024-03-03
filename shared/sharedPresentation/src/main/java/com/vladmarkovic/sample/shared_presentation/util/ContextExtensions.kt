/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.vladmarkovic.sample.shared_presentation.navigation.TopNavigationActionHandler
import com.vladmarkovic.sample.shared_presentation.screen.ToScreenGroup

val Context.asActivity: Activity get() = asActivity<Activity>()

inline fun <reified T: Activity> Context.asActivity(): T = this.let {
        var context = it
        while (context is ContextWrapper) {
            if (context is T) return@let context
            context = context.baseContext
        }
        throw IllegalStateException("Expected an activity context but instead found: $context")
    }

val Context.asTopNavHandler: TopNavigationActionHandler get() = applicationContext as TopNavigationActionHandler

fun Context.handleTopScreenNavigationAction(action: ToScreenGroup) {
    asTopNavHandler.handleTopScreenNavigationAction(asActivity, action)
}
