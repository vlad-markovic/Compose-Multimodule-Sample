/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_presentation.util.onBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ComposeNavArgs(val navController: NavHostController, val scaffoldState: ScaffoldState)

internal fun ComposeNavArgs.isDrawerOpen(): Boolean = scaffoldState.drawerState.currentValue == DrawerValue.Open
internal fun ComposeNavArgs.closeDrawer(scope: CoroutineScope) { scope.launch { scaffoldState.drawerState.close() } }
internal fun ComposeNavArgs.openDrawer(scope: CoroutineScope) { scope.launch { scaffoldState.drawerState.open() } }
internal fun ComposeNavArgs.onBack(scope: CoroutineScope) {
    navController.onBack(
        isDrawerOpen = ::isDrawerOpen,
        closeDrawer = { closeDrawer(scope) }
    )
}

@Composable
fun rememberComposeNavArgs(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    key: Any? = null,
): ComposeNavArgs = remember(key) {
    ComposeNavArgs(navController, scaffoldState)
}