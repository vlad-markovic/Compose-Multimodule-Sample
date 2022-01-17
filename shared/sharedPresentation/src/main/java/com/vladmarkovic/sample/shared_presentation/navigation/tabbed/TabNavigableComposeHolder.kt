/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.activity.ComponentActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.navigation.NavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.drop
import com.vladmarkovic.sample.shared_presentation.util.navigate
import com.vladmarkovic.sample.shared_presentation.util.observeNonNull
import kotlinx.coroutines.CoroutineScope

/** For decorating an Activity to give it Tabbed Compose Navigation functionality. */
@Suppress("FunctionName")
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
            tab.drop(1).observeNonNull(this) {
                navController.navigate(it)
            }
        }
    }

    // region NavigableComposeHolder overrides
    @Composable
    override fun BottomBar() {
        // Don't show tabs if there is only one.
        if (tabs.size == 1) return

        val currentTab by tabNav.tab.observeAsState(mainTab)

        BottomBar(currentTab)
    }

    @Composable
    fun BottomBar(currentTab: T) {
        Lumber.v("recompose currentTab: $currentTab")
        BottomAppBar {
            BottomNavigation {
                tabs.forEach { tab ->
                    Lumber.v("BottomBar, navigation: ${tabNav.hashCode()} ${tab.name}")
                    BottomNavigationItem(
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(stringResource(tab.textRes)) },
                        alwaysShowLabel = false,
                        selected = currentTab == tab,
                        onClick = { if (currentTab != tab) tabNav.navigate(tab) }
                    )
                }
            }
        }
    }

    @Composable
    override fun TopBar(navController: NavHostController) {
        val tab by tabNav.tab.observeAsState(mainTab)
        Lumber.v("recompose")
        tabComposer(tab).ComposeTopBar(navController)
    }

    @Composable
    override fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        val tab by tabNav.tab.observeAsState(mainTab)
        Lumber.v("recompose")
        tabComposer(tab).ComposeDrawer(scaffoldState, mainScope)
    }
    // endregion NavigableComposeHolder overrides
}
