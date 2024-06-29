/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.NavScaffold
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolverEntryPoint
import com.vladmarkovic.sample.shared_presentation.screen.ToTab
import com.vladmarkovic.sample.shared_presentation.util.SetupTabsNavigation
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.util.tabNavigator

@Composable
inline fun <reified T: Tab> TabsNavGraph(
    allTabs: List<T>,
    initialTab: T = allTabs.first(),
    initialScreen: Screen = initialTab.initialScreen,
    navController: NavHostController = rememberNavController(),
    tabNav: TabNavViewModel = tabNavViewModel(tabNavigator(initialTab)),
    noinline bottomBar: @Composable () -> Unit = remember {{ DefaultBottomBar(allTabs, tabNav.currentTab, tabNav::navigate) }},
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }}
) {
    val screenContentResolver: ScreenContentResolver = remember {
        EntryPointAccessor.fromApplication(ScreenContentResolverEntryPoint::class.java).screenContentResolver()
    }
    val tabNavHandler: (BriefAction) -> Unit = remember {{ action ->
        when (action) {
            is ToTab -> tabNav.navigate(action.tab)
            else -> actionHandler(action)
        }
    }}
    NavScaffold(initialScreen, bottomBar, tabNavHandler, navController) { modifier, updateScaffold, bubbleUp ->
        NavHost(
            navController = navController,
            startDestination = initialTab.name,
            modifier = modifier
        ) {
            allTabs.forEach { tab ->
                navigation(
                    startDestination = tab.initialScreen.name,
                    route = tab.name
                ) {
                    composeNavGraph(screenContentResolver, tab.screens, updateScaffold, bubbleUp)
                }
            }
        }
    }
    SetupTabsNavigation(tabs = tabNav.currentTab, navController = navController)
}
