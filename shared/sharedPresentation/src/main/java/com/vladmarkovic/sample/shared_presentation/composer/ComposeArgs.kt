/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.util.onBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal data class ComposeArgs(val navController: NavHostController, val mainScope: CoroutineScope, val scaffoldState: ScaffoldState)

internal val ComposeArgs.isDrawerOpen: Boolean get() = scaffoldState.drawerState.isOpen
internal fun ComposeArgs.closeDrawer() { mainScope.launch { scaffoldState.drawerState.close() } }

internal fun ComposeArgs.openDrawer() { mainScope.launch { scaffoldState.drawerState.open() } }

internal fun ComposeArgs.onBack() { navController.onBack(scaffoldState, mainScope) }
