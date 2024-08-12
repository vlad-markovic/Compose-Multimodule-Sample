/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.screen

import com.vladmarkovic.sample.common.view.action.NavigationAction
import com.vladmarkovic.sample.shared_domain.tab.Tab

/** For typing and scoping tab navigation actions for navigating to [Tab]. */
data class ToTab(val tab: Tab) : NavigationAction
