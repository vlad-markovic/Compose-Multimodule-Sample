package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.onBack
import com.vladmarkovic.sample.shared_presentation.compose.openDrawer
import com.vladmarkovic.sample.shared_presentation.compose.rememberComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.handleAction

@Composable
fun NavScaffold(
    modifier: Modifier = Modifier,
    navArgs: ComposeNavArgs = rememberComposeNavArgs(),
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
    bubbleUp: (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }},
    navHost: @Composable (modifier: Modifier, bubbleUp: (BriefAction) -> Unit) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Black)

    val scope = rememberCoroutineScope()
    val actionHandler: (BriefAction) -> Unit = remember {{ action ->
        when(action) {
            is CommonNavigationAction.Back -> navArgs.onBack(scope)
            is CommonNavigationAction.OpenDrawer -> navArgs.openDrawer(scope)
            else -> navArgs.handleAction(action, bubbleUp)
        }
    }}

    AppTheme {
        Scaffold(
            modifier = modifier,
            scaffoldState = navArgs.scaffoldState,
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
        ) { paddingValues ->
            navHost(Modifier.padding(paddingValues), actionHandler)

            navArgs.BackHandler()
        }
    }
}

@Composable
private fun ComposeNavArgs.BackHandler() {
    val scope = rememberCoroutineScope()
    androidx.activity.compose.BackHandler { onBack(scope) }
}
