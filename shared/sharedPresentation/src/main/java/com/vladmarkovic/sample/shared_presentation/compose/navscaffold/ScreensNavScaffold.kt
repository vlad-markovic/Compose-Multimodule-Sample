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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.composeNavGraph
import com.vladmarkovic.sample.shared_presentation.compose.rememberComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver


@Composable
fun ScreensNavScaffold(
    allScreens: List<NavGraphScreen>,
    modifier: Modifier = Modifier,
    initialScreen: NavGraphScreen = allScreens.first(),
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
    bubbleUp: (BriefAction) -> Unit = rememberThrowingNoHandler(),
) {
    val screenContentResolver: ComposeScreenContentResolver = rememberScreenContentResolver()
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
        NavHost(
            navController = navArgs.navController,
            startDestination = initialScreen.name,
            modifier = Modifier.padding(paddingValues),
        ) {
            composeNavGraph(screenContentResolver, allScreens, actionHandler)
        }
    }
}
