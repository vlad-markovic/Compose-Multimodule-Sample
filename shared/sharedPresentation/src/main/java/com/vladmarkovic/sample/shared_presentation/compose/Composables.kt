package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigable
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.SetupTabsNavigation
import com.vladmarkovic.sample.shared_presentation.util.handleAction
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.util.update
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScreensHolder(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (TopBarData?) -> Unit = { topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    drawer: @Composable (DrawerData?) -> Unit = { topBarData ->
        DefaultDrawer(scaffoldState, mainScope, topBarData)
    },
    bubbleUp: (BriefAction) -> Unit = { throw IllegalStateException("Unhandled action: $it")},
    scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    ScaffoldHolder(navController, scaffoldState, mainScope, topBar, drawer, bottomBar = {}, scaffoldContent, bubbleUp)
}

@Composable
fun Tabs(
    tabs: List<Tab<*>>,
    initialTab: Tab<*> = tabs.first(),
    navGraph: NavGraphBuilder.(
        tab: Tab<*>,
        ContentArgs,
        (ScaffoldChange) -> Unit,
        (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    TabsHolder(tabs, initialTab) { contentArgs, modifier, scaffoldChange, bubbleUp ->
        NavHost(
            navController = contentArgs.navController,
            startDestination = initialTab.name,
            modifier = modifier
        ) {
            tabs.forEach { tab ->
                navigation(
                    startDestination = tab.initialScreen.name,
                    route = tab.name
                ) {
                    navGraph(tab, contentArgs, scaffoldChange, bubbleUp)
                }
            }
        }
    }
}

@Composable
fun TabsHolder(
    tabs: List<Tab<*>>,
    initialTab: Tab<*>,
    tabNav: TabNavigable = tabNavViewModel(initialTab = initialTab),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (TopBarData?) -> Unit = { topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    drawer: @Composable (DrawerData?) -> Unit = { topBarData ->
        DefaultDrawer(scaffoldState, mainScope, topBarData)
    },
    bottomBar: @Composable () -> Unit = {
        DefaultBottomBar(tabs, tabNav.tab, tabNav::navigate)
    },
    bubbleUp: (BriefAction) -> Unit = { throw IllegalStateException("Unhandled action: $it")},
    scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    SetupTabsNavigation(tabs = tabNav.tab, navController = navController)
    ScaffoldHolder(navController, scaffoldState, mainScope, topBar, drawer, bottomBar, scaffoldContent) { action ->
        when (action) {
            is Tab<*> ->  tabNav.navigate(action)
            else -> bubbleUp(action)
        }
    }
}

@Composable
private inline fun ScaffoldHolder(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    crossinline topBar: @Composable (TopBarData?) -> Unit = { topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    crossinline drawer: @Composable (DrawerData?) -> Unit = { topBarData ->
        DefaultDrawer(scaffoldState, mainScope, topBarData)
    },
    noinline bottomBar: @Composable () -> Unit = {},
    crossinline scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit,
    noinline bubbleUp: (BriefAction) -> Unit
) {
    val screenData: MutableState<ScreenData?> = remember { mutableStateOf(null) }
    val topBarData: MutableState<TopBarData?> = remember { mutableStateOf(null) }
    val drawerData: MutableState<DrawerData?> = remember { mutableStateOf(null) }

    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBar(topBarData.value) },
            bottomBar = bottomBar,
            drawerContent = { drawer(drawerData.value) }
        ) { paddingValues ->
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Black)

            val contentArgs = ContentArgs(navController, scaffoldState, mainScope)

            scaffoldContent(
                contentArgs,
                Modifier.padding(paddingValues),
                { scaffoldChange ->
                    screenData.update(scaffoldChange.screenChange)
                    topBarData.update(scaffoldChange.topBarChange.toData(topBarData.value))
                    drawerData.update(scaffoldChange.drawerChange.toData(drawerData.value))
                },
                { holderType, action ->
                    contentArgs.handleAction(holderType, action, bubbleUp)
                }
            )

            androidx.activity.compose.BackHandler {
                screenData.value?.let {
                    contentArgs.handleAction(it.screenHolderType, CommonNavigationAction.Back, bubbleUp)
                } ?: navController.popBackStack()
            }
        }
    }
}
