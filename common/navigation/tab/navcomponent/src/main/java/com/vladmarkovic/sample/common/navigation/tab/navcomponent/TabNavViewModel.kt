/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.tab.navcomponent

import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.di.viewmodel.AssistedViewModelFactory
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** ViewModel for managing navigation between tabs. */
@HiltViewModel(assistedFactory = TabNavViewModelFactory::class)
class TabNavViewModel @AssistedInject constructor(
    @Assisted initialTab: Tab<*>
) : ViewModel() {

    private val _tab = MutableStateFlow(initialTab)

    val tabs: StateFlow<Tab<*>> get() = _tab.asStateFlow()

    fun navigate(tab: Tab<*>) {
        if (_tab.value != tab) {
            _tab.value = tab
        }
    }
}

@AssistedFactory
interface TabNavViewModelFactory : AssistedViewModelFactory<TabNavViewModel, Tab<*>> {
    override fun create(assistedInput: Tab<*>): TabNavViewModel
}
