/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
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
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.rememberComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.tabs
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.util.collectIn
import com.vladmarkovic.sample.shared_presentation.util.navigate
import com.vladmarkovic.sample.shared_presentation.compose.di.rememberScaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.compose.safeValue
import com.vladmarkovic.sample.shared_presentation.compose.di.tabNavViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop

@Composable
inline fun <reified T: Tab> DefaultTabsNavScaffold(
    allTabs: List<T>,
    initialTab: T = allTabs.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialTab.initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, actionHandler)

    val drawerItems = scaffoldData.drawerItems.safeValue
    val showDrawer: Boolean = remember(drawerItems) { drawerItems != null }

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
        drawerContent = remember(showDrawer) {
            drawerItems?.let {{ DefaultDrawer(drawerItems = drawerItems) }}
        },
        actionHandler = scaffoldChangesHandler
    )
}

@Composable
inline fun <reified T: Tab> TabsNavScaffold(
    allTabs: List<T>,
    initialTab: T = allTabs.first(),
    modifier: Modifier = Modifier,
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab, navArgs.navController),
    noinline topBar: @Composable () -> Unit = {},
    noinline bottomBar: @Composable () -> Unit = {},
    noinline snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    noinline floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    noinline drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    drawerShape: Shape = MaterialTheme.shapes.large,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    drawerBackgroundColor: Color = MaterialTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerBackgroundColor),
    drawerScrimColor: Color = DrawerDefaults.scrimColor,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val screenContentResolver: ComposeScreenContentResolver = rememberScreenContentResolver()
    val tabNavHandler: (BriefAction) -> Unit = rememberTabNavHandler(tabNav, actionHandler)
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
    ) { mdfr, bubbleUp ->
        NavHost(
            navController = navArgs.navController,
            startDestination = initialTab.name,
            modifier = mdfr
        ) {
            allTabs.forEach { tab ->
                navigation(
                    startDestination = tab.initialScreen.name,
                    route = tab.name
                ) {
                    composeNavGraph(screenContentResolver, tab.screens, bubbleUp)
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
