package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.compose.DrawerData
import com.vladmarkovic.sample.shared_presentation.compose.TopBarData
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.StateFlow

interface ScaffoldDataMonitor {
    val currentScreen: StateFlow<Screen>
    val holderType: StateFlow<ScreenHolderType>
    val topBar: StateFlow<TopBarData?>
    val drawer: StateFlow<DrawerData?>
}