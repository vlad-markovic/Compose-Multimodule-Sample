package com.vladmarkovic.sample.common.compose.util

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Dp

inline fun Activity.setComposeContentView(crossinline content: @Composable () -> Unit) {
    setContentView(
        ComposeView(this).apply {
            setContent { content() }
        }
    )
}

/** Helper function to be able to specify padding for all and then just override for one or two. */
fun Modifier.padding(
    padding: Dp,
    start: Dp = padding,
    end: Dp = padding,
    top: Dp = padding,
    bottom: Dp = padding
): Modifier = this.then(
    Modifier.padding(start = start, end = end, top = top, bottom = bottom)
)
