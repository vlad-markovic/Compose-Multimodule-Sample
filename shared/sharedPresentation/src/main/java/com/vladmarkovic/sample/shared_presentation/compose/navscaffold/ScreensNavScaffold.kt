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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolverEntryPoint


@Composable
fun ScreensNavScaffold(
    allScreens: List<Screen>,
    modifier: Modifier = Modifier,
    initialScreen: Screen = allScreens.first(),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
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
    actionHandler: (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }},
) {
    val screenContentResolver: ScreenContentResolver = remember {
        EntryPointAccessor.fromApplication(ScreenContentResolverEntryPoint::class.java).screenContentResolver()
    }
    NavScaffold(
        modifier = modifier,
        navController = navController,
        scaffoldState = scaffoldState,
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
        bubbleUp = actionHandler,
    ) { mdfr, bubbleUp ->
        NavHost(
            navController = navController,
            startDestination = initialScreen.name,
            modifier = mdfr,
        ) {
            composeNavGraph(screenContentResolver, allScreens, bubbleUp)
        }
    }
}
