/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vladmarkovic.sample.shared_domain.log.Lumber

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

@Composable
fun LogComposition(what: String) {
    val s = remember {
        val str = "a"
        Lumber.e("COMPOSE $what, str:${str.hashCode()}"); str
    }
    DisposableEffect(Unit) {
        onDispose { Lumber.e("DECOMPOSE $what, str:${s.hashCode()}") }
    }
    Lumber.e("RECOMPOSE $what, str:${s.hashCode()}")
}
