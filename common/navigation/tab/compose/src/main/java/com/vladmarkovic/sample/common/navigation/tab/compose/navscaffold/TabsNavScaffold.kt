/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.tab.compose.navscaffold

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.compose.action.rememberThrowingNoHandler
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.compose.model.ComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.compose.navgraph.composeNavGraph
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.NavScaffold
import com.vladmarkovic.sample.common.navigation.screen.compose.util.rememberComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData
import com.vladmarkovic.sample.common.navigation.tab.compose.rememberTabNavHandler
import com.vladmarkovic.sample.common.navigation.tab.compose.tabNavViewModel
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.TabNavViewModel
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.util.navigate
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.util.route
import com.vladmarkovic.sample.core.coroutines.collectIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop

@Composable
fun <S : Screen, T : Tab<S>> TabsNavScaffold(
    allTabs: List<T>,
    modifier: Modifier = Modifier,
    initialTab: T = allTabs.first(),
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
    bubbleUp: (Action) -> Unit = rememberThrowingNoHandler(),
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        { fadeIn(animationSpec = tween(700)) },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        { fadeOut(animationSpec = tween(700)) },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
    routeDataMap: Map<T, Map<S, ScreenRouteData>>,
    contentResolver: ComposeScreenContentResolver<S>,
) {
    val tabNavHandler: (Action) -> Unit = rememberTabNavHandler(tabNav, bubbleUp)
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
            startDestination = initialTab.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            allTabs.forEach { tab ->
                val data = routeDataMap[tab]!!
                navigation(
                    startDestination = data[tab.initialScreen]!!.routeWithPlaceholders,
                    route = tab.route
                ) {
                    composeNavGraph(tab.screens, actionHandler, data, contentResolver)
                }
            }
        }
    }
    SetupTabsNavigation(tabs = tabNav.tabs, navController = navArgs.navController)
}

@Composable
fun SetupTabsNavigation(tabs: Flow<Tab<*>>, navController: NavController) {
    LaunchedEffect(Unit) {
        tabs.drop(1).collectIn(this) { tab ->
            navController.navigate(tab)
        }
    }
}
