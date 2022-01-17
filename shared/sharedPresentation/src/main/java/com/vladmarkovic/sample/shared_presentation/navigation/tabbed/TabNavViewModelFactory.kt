package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TabNavViewModelFactory<S : Screen, T: Tab<S>> {
    fun create(initial: T): TabNavViewModel<S, T>
}

fun <S : Screen, T: Tab<S>> tabNavViewModelProviderFactory(
    assistedFactory: TabNavViewModelFactory<S, T>,
    initial: T
): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            return assistedFactory.create(initial) as VM
        }
    }
