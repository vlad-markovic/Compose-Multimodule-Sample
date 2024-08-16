package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.action.rememberThrowingNoHandler
import com.vladmarkovic.sample.common.navigation.screen.compose.model.ComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.compose.util.rememberComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData
import com.vladmarkovic.sample.common.navigation.tab.compose.navscaffold.TabsNavScaffold
import com.vladmarkovic.sample.common.navigation.tab.compose.tabNavViewModel
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.compose.animation.enterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.exitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popExitTransition
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.rememberTopActionHandler

@Composable
fun DefaultTabsNavScaffold(
    allTabs: List<Tab>,
    initialTab: Tab = allTabs.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    bubbleUp: (ViewAction) -> Unit = rememberThrowingNoHandler(),
    routeDataResolver: (Screen) -> ScreenRouteData
) {
    val (scaffoldData, topHandler)  = rememberTopActionHandler(initialTab.initialScreen, bubbleUp)

    val data = remember(allTabs) {
        allTabs.associateWith { tab ->
            tab.screens.associateWith { screen ->
                routeDataResolver(screen)
            }
        }
    }

    AppTheme {
        TabsNavScaffold(
            allTabs = allTabs,
            initialTab = initialTab,
            navArgs = navArgs,
            tabNav = tabNav,
            topBar = {
                val topBarData = scaffoldData.topBarData.lifecycleAwareValue
                if (topBarData != null) DefaultTopBar(topBarData) else Unit
            },
            drawerContent = {
                val drawerData = scaffoldData.drawerData.lifecycleAwareValue
                drawerData?.drawerItems?.let { DefaultDrawer(it) }
            },
            bottomBar = remember { { DefaultBottomBar(allTabs, tabNav.tabs, tabNav::navigate) } },
            bubbleUp = topHandler,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() },
            routeDataMap = data
        )
    }
}
