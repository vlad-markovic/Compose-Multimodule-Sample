/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.compose.AnimateFade
import com.vladmarkovic.sample.shared_presentation.compose.BackHandler
import com.vladmarkovic.sample.shared_presentation.compose.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Defines Compose UI and elements for a Screen. */
@Suppress("FunctionName")
abstract class ScreenComposer<VM> where VM : BriefActionable, VM : ViewModel {

    abstract val screen: Screen

    /** Override to specify screen title. Use [titleFromRes] if using string resource. */
    abstract val screenTitle: StateFlow<StrOrRes>

    /** Override to specify up button icon and behaviour action. */
    open val upButton: StateFlow<UpButton?>? = null

    open val drawerItems: StateFlow<List<DrawerItem>>? get() = null

    open val menuItems: StateFlow<Array<MenuItem>>? get() = null

    @Composable
    open fun Content(contentArgs: ContentArgs) {
        Content(contentArgs, viewModel(contentArgs))
    }

    /** Override to specify a [Composable] content for this screen. */
    @Composable
    open fun Content(contentArgs: ContentArgs, viewModel: VM) {
        BackHandler(viewModel)
    }

    @Composable
    abstract fun viewModel(contentArgs: ContentArgs): VM

    @Composable
    fun TopBar(navController: NavHostController) {
        AnimateFade(navController.isScreenVisible) {
            DefaultTopBar(
                title = screenTitle.safeValue,
                up = upButton?.safeValue,
                menuItems = menuItems?.safeValue
            )
        }
    }

    @Composable
    fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        drawerItems?.let {
            DefaultDrawer(it.safeValue) {
                mainScope.launch { scaffoldState.drawerState.close() }
            }
        }
    }

    protected fun titleFromRes(@StringRes res: Int): MutableStateFlow<StrOrRes> =
        MutableStateFlow(StrOrRes.res(res))

    protected fun titleFromStr(str: String): MutableStateFlow<StrOrRes> =
        MutableStateFlow(StrOrRes.str(str))

    val NavController.isScreenVisible: Boolean get() =
        currentDestination?.route?.contains(screen.name) ?: true
}
