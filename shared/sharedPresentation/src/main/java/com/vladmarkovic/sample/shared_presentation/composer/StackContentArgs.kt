/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.util.onBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ContentArgs(
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    val mainScope: CoroutineScope,
)

data class StackContentArgs(
    val contentArgs: ContentArgs,
    val type: ScreenHolderType,
    val backStackEntry: NavBackStackEntry,
    val bubbleUp: (BriefAction) -> Unit,
) {
    val navController: NavController get() = contentArgs.navController
}


val ContentArgs.isDrawerOpen: Boolean get() = scaffoldState.drawerState.isOpen
fun ContentArgs.closeDrawer() {
    mainScope.launch { scaffoldState.drawerState.close() }
}

fun ContentArgs.openDrawer() {
    mainScope.launch { scaffoldState.drawerState.open() }
}

fun ContentArgs.onBack(type: ScreenHolderType) {
    navController.onBack(type, scaffoldState, mainScope)
}
