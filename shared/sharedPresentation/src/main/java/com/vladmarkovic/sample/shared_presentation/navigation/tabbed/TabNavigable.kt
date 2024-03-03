/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.StateFlow

/**
 * For decorating a ViewModel to give it functionality to send [Tab]s as [NavigationAction]s,
 * for enabling composition - ViewModel just needs to instantiate the [TabNavigator];
 * or inherit from [TabNavViewModel].
 */
interface TabNavigable {

    val initialTab: Tab<*>

    val tabNavigator: TabNavigator

    val tab: StateFlow<Tab<*>> get() = tabNavigator.tab

    fun navigate(tab: Tab<*>) {
        tabNavigator.navigateTo(tab)
    }
}
