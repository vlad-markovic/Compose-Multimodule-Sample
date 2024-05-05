/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.NavScaffold
import com.vladmarkovic.sample.shared_presentation.di.ProviderViewModel
import com.vladmarkovic.sample.shared_presentation.di.inject
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.SetupTabsNavigation
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.util.tabNavigator

@Composable
inline fun <reified T: Tab, reified H : ScreenComposerSelectorSelector<T>, reified VMP : ProviderViewModel<H>> TabsNavGraph(
    screensGroupHolder: H = inject<H, VMP>(),
    navController: NavHostController = rememberNavController(),
    tabNav: TabNavViewModel = tabNavViewModel(tabNavigator(screensGroupHolder.initialTab)),
    noinline bottomBar: @Composable () -> Unit = remember {{ DefaultBottomBar(screensGroupHolder.allTabs, tabNav.currentTab, tabNav::navigate) }},
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }}
) {
    val tabNavHandler: (BriefAction) -> Unit = remember {{ action ->
        when (action) {
            is Tab ->  tabNav.navigate(action)
            else -> actionHandler(action)
        }
    }}
    with(screensGroupHolder) {
        NavScaffold(initialScreen, bottomBar, tabNavHandler, navController) { modifier, bubbleUp ->
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
                        composeNavGraph(tab.screenComposerSelector, bubbleUp)
                    }
                }
            }
        }
    }
    SetupTabsNavigation(tabs = tabNav.currentTab, navController = navController)
}

interface ScreenComposerSelectorSelector<T: Tab> {
    val allTabs: List<T>
    val initialTab: Tab get() = allTabs.first()
    val initialScreen: Screen get() = initialTab.initialScreen
    val T.screenComposerSelector: ScreenComposerSelector<*>
}
