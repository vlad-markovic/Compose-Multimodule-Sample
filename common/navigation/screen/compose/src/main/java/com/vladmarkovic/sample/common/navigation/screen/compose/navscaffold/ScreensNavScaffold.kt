/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.compose.action.rememberThrowingNoHandler
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.compose.model.ComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.compose.navgraph.composeNavGraph
import com.vladmarkovic.sample.common.navigation.screen.compose.util.rememberComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData


@Composable
fun <S : Screen> ScreensNavScaffold(
    allScreens: List<S>,
    modifier: Modifier = Modifier,
    initialScreen: S = allScreens.first(),
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
    topBar: @Composable () -> Unit = {},
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
    routeDataResolver: (S) -> ScreenRouteData,
    contentResolver: ComposeScreenContentResolver<S>,
) {
    NavScaffold(
        modifier = modifier,
        navArgs = navArgs,
        topBar = topBar,
        bottomBar = {},
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
        bubbleUp = bubbleUp,
    ) { paddingValues, actionHandler ->
        val data = remember(allScreens) {
            allScreens.associateWith { screen ->
                routeDataResolver(screen)
            }
        }
        NavHost(
            navController = navArgs.navController,
            startDestination = routeDataResolver(initialScreen).routeWithPlaceholders,
            modifier = Modifier.padding(paddingValues),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            composeNavGraph(allScreens, actionHandler, data, contentResolver)
        }
    }
}
