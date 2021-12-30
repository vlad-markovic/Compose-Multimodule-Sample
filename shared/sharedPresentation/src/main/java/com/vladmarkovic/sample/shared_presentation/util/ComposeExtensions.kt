package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

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
