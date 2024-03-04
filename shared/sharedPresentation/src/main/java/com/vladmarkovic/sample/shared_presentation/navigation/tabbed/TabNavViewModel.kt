/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.di.AssistedViewModelFactory
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/** ViewModel for managing navigation between tabs. */
@HiltViewModel(assistedFactory = TabNavViewModelFactory::class)
class TabNavViewModel @AssistedInject constructor(
    @Assisted tabNavigator: TabNavigator
) : ViewModel(), TabNavigable by tabNavigator, TabMonitor by tabNavigator

@AssistedFactory
interface TabNavViewModelFactory : AssistedViewModelFactory<TabNavViewModel, TabNavigator> {
    override fun create(assistedInput: TabNavigator): TabNavViewModel
}
