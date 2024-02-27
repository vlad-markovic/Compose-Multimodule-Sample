/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.StateFlow

/** Holds and updates the state for the current screen. */
interface CurrentScreenMonitor<S: Screen> {
    /** Override providing entries of the scoped screens enum */
    val allScreens: List<S>
    /** Set to first screen to be initial screen */
    val initialScreen: S
    /** Set to first screen to be initial screen. Used to determine Drawer and TopBar for each screen. */
    val currentScreen: StateFlow<S>
    fun updateCurrentScreen(screen: S)
}
