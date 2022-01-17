/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.annotation.StringRes
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.compose.AnimateFade
import com.vladmarkovic.sample.shared_presentation.compose.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Defines Compose UI and elements for a Screen. */
@Suppress("FunctionName")
abstract class ScreenComposer {

    /** Override to specify screen title. Use [titleFromRes] if using string resource. */
    abstract val screenTitle: State<StrOrRes>

    abstract val screen: Screen

    /** Override to specify up button icon and behaviour action. */
    open val upButton: State<UpButton?>? = null

    open val drawerItems: State<List<DrawerItem>>? get() = null

    /** Override to specify a [Composable] content for this screen. */
    @Composable
    abstract fun Content(contentArgs: ContentArgs)

    @Composable
    fun TopBar(navController: NavHostController) {
        AnimateFade(navController.isScreenVisible) {
            DefaultTopBar(screenTitle, up = upButton)
        }
    }

    @Composable
    fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        drawerItems?.let {
            DefaultDrawer(it) {
                mainScope.launch { scaffoldState.drawerState.close() }
            }
        }
    }

    protected fun titleFromRes(@StringRes res: Int): MutableState<StrOrRes> =
        mutableStateOf(StrOrRes.res(res))

    val NavController.isScreenVisible: Boolean get() =
        currentDestination?.route?.contains(screen.name) ?: true
}
