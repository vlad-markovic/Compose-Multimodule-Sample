/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_presentation.util.onBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ComposeNavArgs(
    val navController: NavHostController,
    val mainScope: CoroutineScope,
    val scaffoldState: ScaffoldState
)

internal val ComposeNavArgs.isDrawerOpen: Boolean get() = scaffoldState.drawerState.isOpen
internal fun ComposeNavArgs.closeDrawer() { mainScope.launch { scaffoldState.drawerState.close() } }

internal fun ComposeNavArgs.openDrawer() { mainScope.launch { scaffoldState.drawerState.open() } }

internal fun ComposeNavArgs.onBack() { navController.onBack(scaffoldState, mainScope) }

@Composable
fun rememberComposeNavArgs(
    navController: NavHostController = rememberNavController(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    key1: Any? = null,
    key2: Any? = null,
    key3: Any? = null,
): ComposeNavArgs = remember(key1, key2, key3) {
    ComposeNavArgs(navController, mainScope, scaffoldState)
}