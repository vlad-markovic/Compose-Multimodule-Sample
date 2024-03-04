/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.briefaction.action
import com.vladmarkovic.sample.shared_presentation.compose.DrawerChange
import com.vladmarkovic.sample.shared_presentation.compose.OnStart
import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange
import com.vladmarkovic.sample.shared_presentation.compose.ScreenData
import com.vladmarkovic.sample.shared_presentation.compose.TopBarChange
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton

/** Defines Compose UI and elements for a Screen. */
abstract class ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {

    abstract val screen: Screen

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
    fun Content(args: ScreenArgs, holderType: ScreenHolderType) {
        val viewModel = viewModel(args)
        scaffoldChange(viewModel, holderType)?.let { screenChange ->
            OnStart { viewModel.action(screenChange) }
        }
        // BackHandler(viewModel) // TODO? If cannot, otherwise remove [viewModel] method, and let each inject VM.
        Content(args, viewModel)
    }


    /** Override to specify a [Composable] content for this screen. */
    @Composable
    protected abstract fun Content(args: ScreenArgs, viewModel: VM)

    @Composable
    abstract fun viewModel(args: ScreenArgs): VM

    protected abstract fun scaffoldChange(viewModel: VM, holderType: ScreenHolderType): ScreenChange?

    protected fun change(holderType: ScreenHolderType, title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        ScreenChange(ScreenData(screen, holderType), topBarChange(title, upButton, menuItems))

    protected fun change(holderType: ScreenHolderType, topBarChange: TopBarChange, drawerItems: List<DrawerItem>? = null) =
        ScreenChange(ScreenData(screen, holderType), topBarChange, drawerItems?.let { DrawerChange(it) })

    protected fun topBarChange(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        TopBarChange(screen, title, upButton, menuItems)

    protected fun drawerChange(drawerItems: List<DrawerItem>? = null) =
        drawerItems?.let { DrawerChange(it) }
}
