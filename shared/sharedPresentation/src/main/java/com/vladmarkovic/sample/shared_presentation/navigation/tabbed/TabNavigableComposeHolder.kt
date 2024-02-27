/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.activity.ComponentActivity
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.navigation.NavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.collectWith
import com.vladmarkovic.sample.shared_presentation.util.navigate
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop

/** For decorating an Activity to give it Tabbed Compose Navigation functionality. */
interface TabNavigableComposeHolder<S : Screen, T : Tab<S>> : NavigableComposeHolder<S> {

    val tabNavViewModelFactory: TabNavViewModelFactory<S, T>

    val tabNav: TabNavigable<S, T> // TabNavViewModel

    val tabs: List<T>

    val mainTab: T get() = tabs.first()

    override val initialScreen: S get() = mainTab.initialScreen

    override val initialDestination: String get() = mainTab.name

    fun tabComposer(tab: T): ScreenHolderComposer<S>

    override fun NavGraphBuilder.navGraph(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope
    ) {
        tabNav.setupWith(navController)
        tabs.forEach { tab ->
            navigation(
                startDestination = tab.initialScreen.name,
                route = tab.name
            ) {
                with(tabComposer(tab)) {
                    composeNavGraph(navController, scaffoldState, mainScope)
                }
            }
        }
    }

    fun setCurrentTab(tab: T) {
        tabNav.navigate(tab)
    }

    private fun TabNavigable<S, T>.setupWith(navController: NavHostController) {
        with(this@TabNavigableComposeHolder as ComponentActivity) {
            tab.drop(1).collectWith(this) {
                navController.navigate(it)
            }
        }
    }

    // region NavigableComposeHolder overrides
    @Composable
    override fun BottomBar() {
        // Don't show tabs if there is only one.
        if (tabs.size == 1) return

        BottomBar(tabNav.tab.safeValue)
    }

    @Composable
    fun BottomBar(currentTab: T) {
        BottomAppBar {
            BottomNavigation {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(stringResource(tab.textRes)) },
                        alwaysShowLabel = false,
                        selected = currentTab == tab,
                        onClick = { tabNav.navigate(tab) }
                    )
                }
            }
        }
    }

    @Composable
    override fun TopBar(navController: NavHostController) {
        val tab = tabNav.tab.safeValue
        tabComposer(tab).ComposeTopBar(navController)
    }

    @Composable
    override fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        tabComposer(tabNav.tab.safeValue).ComposeDrawer(scaffoldState, mainScope)
    }
    // endregion NavigableComposeHolder overrides
}
