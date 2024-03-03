/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** ViewModel for managing navigation between tabs. */
class TabNavViewModel @AssistedInject constructor(
    @Assisted override val initialTab: Tab<*>
) : ViewModel(), TabNavigable {

    override val tabNavigator: TabNavigator = TabNavigator(initialTab)
}
