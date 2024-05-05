package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.Screen
import kotlinx.coroutines.flow.StateFlow

interface ScaffoldDataMonitor {
    val currentScreen: StateFlow<Screen?>
    val topBar: StateFlow<(@Composable () -> Unit)?>
    val drawer: StateFlow<(@Composable ColumnScope.() -> Unit)?>
}
