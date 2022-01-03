/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

inline fun Activity.setComposeContentView(crossinline content: @Composable () -> Unit) {
    setContentView(
        ComposeView(this).apply {
            setContent { content() }
        }
    )
}
