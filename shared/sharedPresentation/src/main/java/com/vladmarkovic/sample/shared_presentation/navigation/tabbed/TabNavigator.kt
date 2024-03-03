/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Manages tab navigation on tab press. State-full (uses LiveData) to keep track of current tab. */
class TabNavigator(initialTab: Tab<*>)  {

    private val _tab = MutableStateFlow(initialTab)
    val tab: StateFlow<Tab<*>> = _tab.asStateFlow()

    fun navigateTo(tab: Tab<*>) {
        if (_tab.value != tab) {
            _tab.value = tab
        }
    }
}
