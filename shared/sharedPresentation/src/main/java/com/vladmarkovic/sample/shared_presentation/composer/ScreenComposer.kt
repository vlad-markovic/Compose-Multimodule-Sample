/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.compose.DrawerChange
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
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
    fun Content(args: ScreenArgs, asSuper: @Composable () -> Unit) {
        asSuper()
        Content(args, viewModel(args))
    }


    /** Override to specify a [Composable] content for this screen. */
    @Composable
    fun Content(args: ScreenArgs, viewModel: VM) {
//        BackHandler(viewModel)
    }

    @Composable
    fun viewModel(args: ScreenArgs): VM

    @Composable
    fun SetupScreen(screenSetup: (ScaffoldChange) -> Unit, change: ScaffoldChange) {
        LaunchedEffect(Unit) {
            screenSetup(change)
        }
    }

    fun change(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        ScaffoldChange(screen, topBarChange(title, upButton, menuItems))

    fun change(topBarChange: TopBarChange, drawerItems: List<DrawerItem>? = null) =
        ScaffoldChange(screen, topBarChange, drawerItems?.let { DrawerChange(it) })

    fun topBarChange(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        TopBarChange(screen, title, upButton, menuItems)

    val NavController.isScreenVisible: Boolean get() = isScreenVisible(screen.name)
}
