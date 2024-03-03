/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.util.onBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ComposeArgs(
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    val mainScope: CoroutineScope,
)

data class ScreenArgs(
    val composeArgs: ComposeArgs,
    val screenSetup: (ScaffoldChange) -> Unit,
    val bubbleUp: (BriefAction) -> Unit,
) {
    val navController: NavHostController get() = composeArgs.navController
}


val ComposeArgs.isDrawerOpen: Boolean get() = scaffoldState.drawerState.isOpen
fun ComposeArgs.closeDrawer() {
    mainScope.launch { scaffoldState.drawerState.close() }
}

fun ComposeArgs.openDrawer() {
    mainScope.launch { scaffoldState.drawerState.open() }
}

fun ComposeArgs.onBack(type: ScreenHolderType) {
    navController.onBack(type, scaffoldState, mainScope)
}
