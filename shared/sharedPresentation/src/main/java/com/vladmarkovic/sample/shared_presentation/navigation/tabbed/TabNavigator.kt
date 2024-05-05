/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Manages tab navigation on tab press. State-full (uses StateFlow) to keep track of current tab. */
class TabNavigator @AssistedInject constructor(@Assisted initialTab: Tab) : TabNavigable, TabMonitor  {

    private val tab = MutableStateFlow(initialTab)
    override val currentTab: StateFlow<Tab> = tab.asStateFlow()

    override fun navigate(tab: Tab) {
        if (this.tab.value != tab) {
            this.tab.value = tab
        }
    }
}

@AssistedFactory
interface TabNavigatorFactory : BaseAssistedFactory<TabNavigator, Tab> {
    override fun create(assistedInput: Tab): TabNavigator
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface TabNavigatorFactoryProvider {
    fun provideTabNavigatorFactory(): TabNavigatorFactory
}

