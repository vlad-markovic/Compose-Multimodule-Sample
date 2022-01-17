/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** ViewModel for managing navigation between tabs. */
class TabNavViewModel<S : Screen, T: Tab<S>> @AssistedInject constructor(
    @Assisted override val initialTab: T
) : ViewModel(), TabNavigable<S, T> {

    override val tabNavigator: TabNavigator<S, T> = TabNavigator(initialTab)
}
