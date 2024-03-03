/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import dagger.assisted.AssistedFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@AssistedFactory
interface TabNavViewModelFactory {
    fun create(initial: Tab<*>): TabNavViewModel
}

fun tabNavViewModelProviderFactory(
    assistedFactory: TabNavViewModelFactory,
    initial: Tab<*>
): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            @Suppress("UNCHECKED_CAST")
            return assistedFactory.create(initial) as VM
        }
    }

@EntryPoint
@InstallIn(ActivityComponent::class)
interface TabNavViewModelFactoryProvider {
    fun tabNavViewModelProviderFactory(): TabNavViewModelFactory
}
