package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.composer.ComposeArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.ScaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.defaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.SetupTabsNavigation
import com.vladmarkovic.sample.shared_presentation.util.handleAction
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import com.vladmarkovic.sample.shared_presentation.util.scaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.util.tabNavigator
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScreensHolder(
    initialScreen: Screen,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (TopBarData?) -> Unit = { topBarData -> DefaultTopBar(topBarData) },
    drawer: (DrawerData?) -> (@Composable ColumnScope.() -> Unit)? = { drawerData ->
        defaultDrawer(scaffoldState, mainScope, drawerData)
    },
    bubbleUp: (BriefAction) -> Unit = { throw IllegalStateException("Unhandled action: $it")},
    scaffoldContent: @Composable (args: ScreenArgs, modifier: Modifier) -> Unit
) {
    ScaffoldHolder(initialScreen, navController, scaffoldState, mainScope, topBar, drawer, bottomBar = {}, scaffoldContent, bubbleUp)
}

@Composable
fun Tabs(
    initialScreen: Screen,
    tabs: List<Tab<*>>,
    initialTab: Tab<*> = tabs.first(),
    navGraph: NavGraphBuilder.(tab: Tab<*>, ScreenArgs) -> Unit
) {
    TabsHolder(initialScreen, tabs, initialTab) { args, modifier ->
        NavHost(
            navController = args.navController,
            startDestination = initialTab.name,
            modifier = modifier
        ) {
            tabs.forEach { tab ->
                navigation(
                    startDestination = tab.initialScreen.name,
                    route = tab.name
                ) {
                    navGraph(tab, args)
                }
            }
        }
    }
}

@Composable
fun TabsHolder(
    initialScreen: Screen,
    tabs: List<Tab<*>>,
    initialTab: Tab<*>,
    tabNav: TabNavViewModel = tabNavViewModel(tabNavigator(initialTab)),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (TopBarData?) -> Unit = { topBarData -> DefaultTopBar(topBarData) },
    drawer: (DrawerData?) -> (@Composable ColumnScope.() -> Unit)? = { drawerData ->
        defaultDrawer(scaffoldState, mainScope, drawerData)
    },
    bottomBar: @Composable () -> Unit = { DefaultBottomBar(tabs, tabNav.currentTab, tabNav::navigate) },
    bubbleUp: (BriefAction) -> Unit = { throw IllegalStateException("Unhandled action: $it")},
    scaffoldContent: @Composable (args: ScreenArgs, modifier: Modifier) -> Unit
) {
    SetupTabsNavigation(tabs = tabNav.currentTab, navController = navController)
    ScaffoldHolder(initialScreen, navController, scaffoldState, mainScope, topBar, drawer, bottomBar, scaffoldContent) { action ->
        when (action) {
            is Tab<*> ->  tabNav.navigate(action)
            else -> bubbleUp(action)
        }
    }
}

@Composable
private fun ScaffoldHolder(
    initialScreen: Screen,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (TopBarData?) -> Unit = { topBarData -> DefaultTopBar(topBarData) },
    drawer: (DrawerData?) -> (@Composable ColumnScope.() -> Unit)? = { drawerData ->
        defaultDrawer(scaffoldState, mainScope, drawerData)
    },
    bottomBar: @Composable () -> Unit = {},
    scaffoldContent: @Composable (args: ScreenArgs, modifier: Modifier) -> Unit,
    bubbleUp: (BriefAction) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Black)

    val composeArgs = ComposeArgs(navController, scaffoldState, mainScope)

    val scaffoldData: ScaffoldDataManager = scaffoldDataManager(initialScreen)

    val actionHandler: (BriefAction) -> Unit = remember {{ action ->
        when (action) {
            is ScreenChange -> scaffoldData.update(action)
            else -> bubbleUp(action)
        }
    }}

    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBar(scaffoldData.topBar.safeValue) },
            bottomBar = bottomBar,
            drawerContent = drawer(scaffoldData.drawer.safeValue)
        ) { paddingValues ->
            val holderType = scaffoldData.holderType.safeValue
            scaffoldContent(
                ScreenArgs(composeArgs) { composeArgs.handleAction(holderType, it, actionHandler) },
                Modifier.padding(paddingValues)
            )
        }

        BackHandler(composeArgs, scaffoldData.holderType.safeValue, actionHandler)
    }
}

@Composable
private fun BackHandler(composeArgs: ComposeArgs, holderType: ScreenHolderType, actionHandler: (BriefAction) -> Unit) {
    androidx.activity.compose.BackHandler {
        composeArgs.handleAction(holderType, CommonNavigationAction.Back, actionHandler)
    }
}
