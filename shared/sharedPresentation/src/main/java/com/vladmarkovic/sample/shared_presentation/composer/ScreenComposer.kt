/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.briefaction.action
import com.vladmarkovic.sample.shared_presentation.compose.DrawerChange
import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange
import com.vladmarkovic.sample.shared_presentation.compose.TopBarChange
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel

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

    @CallSuper
    @Composable
    open fun Content(args: ScreenArgs, asSuper: @Composable () -> Unit) {
        val vm2 = actionViewModel<BriefActionViewModel>(args.bubbleUp)
        Lumber.e("Content ${this.javaClass.simpleName}, ${vm2.javaClass.simpleName}2 ${vm2.hashCode()}")

        asSuper()
        Content(args, viewModel(args))
    }


    /** Override to specify a [Composable] content for this screen. */
    @CallSuper
    @Composable
    protected open fun Content(args: ScreenArgs, viewModel: VM) {
        Lumber.e("Content ${this.javaClass.simpleName}, ${viewModel.javaClass.simpleName} ${viewModel.hashCode()}")
        scaffoldChange(viewModel)?.let {
            LaunchedEffect(viewModel) {
//                args.bubbleUp(it)
                viewModel.action(it)
            }
        }
//        BackHandler(viewModel) // TODO? If cannot, otherwise remove [viewModel] method, and let each inject VM.
    }

    @Composable
    abstract fun viewModel(args: ScreenArgs): VM

    protected abstract fun scaffoldChange(viewModel: VM): ScreenChange?

    protected fun change(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        ScreenChange(screen, topBarChange(title, upButton, menuItems))

    protected fun change(topBarChange: TopBarChange, drawerItems: List<DrawerItem>? = null) =
        ScreenChange(screen, topBarChange, drawerItems?.let { DrawerChange(it) })

    protected fun topBarChange(title: StrOrRes? = null, upButton: UpButton? = null, menuItems: List<MenuItem>? = null) =
        TopBarChange(screen, title, upButton, menuItems)
}
