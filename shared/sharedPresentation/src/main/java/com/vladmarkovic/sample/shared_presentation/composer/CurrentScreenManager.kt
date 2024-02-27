/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach

class CurrentScreenManager<S: Screen>(
    override val allScreens: List<S>,
    override val initialScreen: S = allScreens.first()
) : CurrentScreenMonitor<S> {

    private val _currentScreen: MutableStateFlow<S> = MutableStateFlow(initialScreen)

    override val currentScreen: StateFlow<S> = _currentScreen.asStateFlow()
        .also {
            it.onEach {
                Lumber.e("CovidTabComposer next screen: $it")
            }
        }

    override fun updateCurrentScreen(screen: S) {
        if (_currentScreen.value != screen) {
            _currentScreen.value = screen
        }
    }
}
