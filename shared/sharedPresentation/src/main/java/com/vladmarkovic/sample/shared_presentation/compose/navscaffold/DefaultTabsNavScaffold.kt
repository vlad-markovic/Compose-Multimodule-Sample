package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_presentation.compose.animation.enterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.exitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popExitTransition
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.di.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.rememberComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.tabs
import com.vladmarkovic.sample.shared_presentation.screen.ScreenRouteData

@Composable
fun DefaultTabsNavScaffold(
    allTabs: List<Tab>,
    initialTab: Tab = allTabs.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    bubbleUp: (ViewAction) -> Unit = rememberThrowingNoHandler(),
    routeDataResolver: (NavGraphScreen) -> ScreenRouteData
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialTab.initialScreen)
    val scaffoldChangesHandler: (ViewAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, bubbleUp)

    val data = remember(allTabs) {
        allTabs.associateWith { tab ->
            tab.screens.associateWith { screen ->
                routeDataResolver(screen)
            }
        }
    }

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
        bubbleUp = scaffoldChangesHandler,
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
        routeDataMap = data
    )
}