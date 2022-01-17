/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.screen.Screen

inline fun Activity.setComposeContentView(crossinline content: @Composable () -> Unit) {
    setContentView(
        ComposeView(this).apply {
            setContent { content() }
        }
    )
}

@Suppress("UNCHECKED_CAST")
fun <S: Screen> Activity.setCurrentTab(tab: Tab<S>) {
    (this as TabNavigableComposeHolder<S, Tab<S>>).setCurrentTab(tab)
}
