/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.tab.navcomponent.model

import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.mv.action.NavigationAction

/** For typing and scoping tab navigation actions for navigating to [Tab]. */
data class ToTab(val tab: Tab) : NavigationAction
