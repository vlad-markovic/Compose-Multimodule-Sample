package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.LiveData
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen

/**
 * For decorating a ViewModel to give it functionality to send [Tab]s as [NavigationAction]s,
 * for enabling composition - ViewModel just needs to instantiate the [TabNavigator];
 * or inherit from [TabNavViewModel].
 */
interface TabNavigable<S : Screen, T: Tab<S>> {

    val initialTab: T

    val tabNavigator: TabNavigator<S, T>

    val tab: LiveData<T> get() = tabNavigator.tab

    fun navigate(tab: T) {
        tabNavigator.navigateTo(tab)
    }
}
