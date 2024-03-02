/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.compose.DrawerChange
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.compose.ScreenData
import com.vladmarkovic.sample.shared_presentation.compose.TopBarChange
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.isScreenVisible

/** Defines Compose UI and elements for a Screen. */
interface ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {

    val screen: Screen
    val type: ScreenHolderType get() = ScreenHolderType.STANDALONE

//    @Composable
//    fun Main() {
//
//    }
//
//    @Composable
//    fun TopBar(navController: NavHostController, topBarData: TopBarData?) {
//        DefaultTopBar(navController, topBarData)
//    }
//
//    @Composable
//    fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope, drawerData: DrawerData?) {
//        DefaultDrawer(scaffoldState, mainScope, drawerData)
//    }

    @Composable
    fun Content(stackContentArgs: StackContentArgs, screenSetup: (ScaffoldChange) -> Unit) {
        Content(stackContentArgs, screenSetup, viewModel(stackContentArgs))
    }


    /** Override to specify a [Composable] content for this screen. */
    @Composable
    fun Content(stackContentArgs: StackContentArgs, screenSetup: (ScaffoldChange) -> Unit, viewModel: VM) {
//        BackHandler(viewModel)
    }

    @Composable
    fun viewModel(stackContentArgs: StackContentArgs): VM

    @Composable
    fun SetupScreen(screenSetup: (ScaffoldChange) -> Unit, change: ScaffoldChange) {
        LaunchedEffect(Unit) {
            screenSetup(change)
        }
    }

    fun change(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        ScaffoldChange(ScreenData(screen, type), topBarChange(title, upButton, menuItems))

    fun change(topBarChange: TopBarChange, drawerItems: List<DrawerItem>? = null) =
        ScaffoldChange(ScreenData(screen, type), topBarChange, drawerItems?.let { DrawerChange(it) })

    fun topBarChange(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        TopBarChange(screen, title, upButton, menuItems)

    val NavController.isScreenVisible: Boolean get() = isScreenVisible(screen.name)
}
