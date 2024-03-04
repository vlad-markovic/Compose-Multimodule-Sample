/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab

/**
 * For decorating a ViewModel to give it functionality to send [Tab]s as [NavigationAction]s,
 * for enabling composition - ViewModel just needs to instantiate the [TabNavigator];
 * or inherit from [TabNavViewModel].
 */
interface TabNavigable {
    fun navigate(tab: Tab<*>)
}
