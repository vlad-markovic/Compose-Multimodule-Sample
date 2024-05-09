/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_domain.tab.Tab
import kotlinx.coroutines.flow.StateFlow

interface TabMonitor  {
    val currentTab: StateFlow<Tab>
}
