/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.compose.di.tabNavViewModel
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.rememberComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.safeValue
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.tabs
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.util.collectIn
import com.vladmarkovic.sample.shared_presentation.util.navigate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop

@Composable
fun DefaultTabsNavScaffold(
    allTabs: List<Tab>,
    initialTab: Tab = allTabs.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    bubbleUp: (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialTab.initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, bubbleUp)

    val drawerData = scaffoldData.drawerData.safeValue

    TabsNavScaffold(
        allTabs = allTabs,
        initialTab = initialTab,
        navArgs = navArgs,
        tabNav = tabNav,
        topBar = remember {{
            DefaultTopBar(
                data = scaffoldData.topBarData.safeValue,
                backgroundColor = AppColor.Grey900
            )
        }},
        bottomBar = remember {{ DefaultBottomBar(allTabs, tabNav.tabs, tabNav::navigate) }},
        drawerContent = remember(drawerData) { drawerData?.drawerItems?.let {{ DefaultDrawer(it) }} },
        bubbleUp = scaffoldChangesHandler
    )
}

@Composable
fun TabsNavScaffold(
    allTabs: List<Tab>,
    modifier: Modifier = Modifier,
    initialTab: Tab = allTabs.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    bubbleUp: (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val screenContentResolver: ComposeScreenContentResolver = rememberScreenContentResolver()
    val tabNavHandler: (BriefAction) -> Unit = rememberTabNavHandler(tabNav, bubbleUp)
    NavScaffold(
        modifier = modifier,
        navArgs = navArgs,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        isFloatingActionButtonDocked = isFloatingActionButtonDocked,
        drawerContent = drawerContent,
        drawerGesturesEnabled = drawerGesturesEnabled,
        drawerShape = drawerShape,
        drawerElevation = drawerElevation,
        drawerBackgroundColor = drawerBackgroundColor,
        drawerContentColor = drawerContentColor,
        drawerScrimColor = drawerScrimColor,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        bubbleUp = tabNavHandler,
    ) { paddingValues, actionHandler ->
        NavHost(
            navController = navArgs.navController,
            startDestination = initialTab.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            allTabs.forEach { tab ->
                navigation(
                    startDestination = tab.initialScreen.name,
                    route = tab.name
                ) {
                    composeNavGraph(screenContentResolver, tab.screens, actionHandler)
                }
            }
        }
    }
    SetupTabsNavigation(tabs = tabNav.tabs, navController = navArgs.navController)
}

@Composable
fun SetupTabsNavigation(tabs: Flow<Tab>, navController: NavController) {
    LaunchedEffect(Unit) {
        tabs.drop(1).collectIn(this) { tab ->
            navController.navigate(tab)
        }
    }
}
