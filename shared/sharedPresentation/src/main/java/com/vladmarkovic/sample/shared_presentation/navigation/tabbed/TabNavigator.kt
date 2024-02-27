/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Manages tab navigation on tab press. State-full (uses LiveData) to keep track of current tab. */
class TabNavigator<S: Screen, T: Tab<S>>(initialTab: T)  {

    private val _tab = MutableStateFlow(initialTab)
    val tab: StateFlow<T> = _tab.asStateFlow()

    fun navigateTo(tab: T) {
        _tab.value = tab
    }
}
