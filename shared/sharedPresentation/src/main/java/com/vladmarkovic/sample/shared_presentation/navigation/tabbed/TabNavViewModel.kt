/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.di.viewmodel.AssistedViewModelFactory
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** ViewModel for managing navigation between tabs. */
@HiltViewModel(assistedFactory = TabNavViewModelFactory::class)
class TabNavViewModel @AssistedInject constructor(
    @Assisted initialTab: Tab
) : ViewModel(), MutableStateFlow<Tab> by MutableStateFlow(initialTab)

@AssistedFactory
interface TabNavViewModelFactory : AssistedViewModelFactory<TabNavViewModel, Tab> {
    override fun create(assistedInput: Tab): TabNavViewModel
}

/**
 * To give ViewModel functionality to send [Tab]s as [NavigationAction]s.
 * ViewModel needs to be implementing MutableStateFlow<Tab>.
 */
fun <T> T.navigate(tab: Tab) where T : ViewModel, T : MutableStateFlow<Tab> {
    if (value != tab) {
        value = tab
    }
}

val StateFlow<Tab>.tabs: StateFlow<Tab> get() = this
