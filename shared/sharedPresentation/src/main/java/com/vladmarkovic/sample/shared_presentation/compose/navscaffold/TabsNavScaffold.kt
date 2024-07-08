/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultBottomBar
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolverEntryPoint
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.screen.ToTab
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.util.SetupTabsNavigation
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import com.vladmarkovic.sample.shared_presentation.util.scaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.util.tabNavViewModel

@Composable
inline fun <reified T: Tab> DefaultTabsNavScaffold(
    allTabs: List<T>,
    initialTab: T = allTabs.first(),
    tabNav: TabNavViewModel = tabNavViewModel(initialTab),
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = remember {
        { throw IllegalStateException("Unhandled action: $it") }
    },
) {
    val scaffoldData: ScaffoldDataManager = scaffoldDataManager(initialTab.initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = remember {{ action ->
        when (action) {
            is ScaffoldData -> scaffoldData.update(action)
            else -> actionHandler(action)
        }
    }}

    val drawerItems = scaffoldData.drawerItems.safeValue

    TabsNavScaffold(
        allTabs = allTabs,
        initialTab = initialTab,
        tabNav = tabNav,
        topBar = remember {{
            DefaultTopBar(
                data = scaffoldData.topBarData.safeValue,
                backgroundColor = AppColor.Grey900
            )
        }},
        bottomBar = remember {{ DefaultBottomBar(allTabs, tabNav, tabNav::navigate) }},
        drawerContent = remember(drawerItems) {
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
    tabNav: TabNavViewModel = tabNavViewModel(initialTab),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
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
    noinline actionHandler: @DisallowComposableCalls (BriefAction) -> Unit = remember {
        { throw IllegalStateException("Unhandled action: $it") }
    },
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
    NavScaffold(
        modifier = modifier,
        navController = navController,
        scaffoldState = scaffoldState,
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
            navController = navController,
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
    SetupTabsNavigation(tabs = tabNav, navController = navController)
}
