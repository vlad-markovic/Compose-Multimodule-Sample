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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.composer.ComposeArgs
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.ScaffoldDataManager
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.handleAction
import com.vladmarkovic.sample.shared_presentation.util.safeValue
import com.vladmarkovic.sample.shared_presentation.util.scaffoldDataManager
import kotlinx.coroutines.CoroutineScope
import java.util.Optional


data class ScaffoldChange(
    val screen: Screen,
    val topBar: Optional<@Composable () -> Unit>?,
    val drawer: Optional<(@Composable ColumnScope.() -> Unit)>?,
) : BriefAction


@Composable
fun NavScaffold(
    initialScreen: Screen,
    bottomBar: @Composable () -> Unit,
    bubbleUp: (BriefAction) -> Unit,
    navController: NavHostController = rememberNavController(),
    navHost: @Composable (modifier: Modifier, bubbleUp: (BriefAction) -> Unit) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(Color.Black)

    val mainScope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val scaffoldData: ScaffoldDataManager = scaffoldDataManager(initialScreen)

    val composeArgs = ComposeArgs(navController, mainScope, scaffoldState)

    val actionHandler: (BriefAction) -> Unit = remember {{ action ->
        when (action) {
            is ScaffoldChange -> scaffoldData.update(action)
            else -> bubbleUp(action)
        }
    }}

    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = scaffoldData.topBar.safeValue ?: {},
            bottomBar = bottomBar,
            drawerContent = scaffoldData.drawer.safeValue
        ) { paddingValues ->
            navHost(Modifier.padding(paddingValues)) { composeArgs.handleAction(it, actionHandler) }

            composeArgs.BackHandler(actionHandler)
        }
    }
}

@Composable
private fun ComposeArgs.BackHandler(actionHandler: (BriefAction) -> Unit) {
    androidx.activity.compose.BackHandler {
        handleAction(CommonNavigationAction.Back, actionHandler)
    }
}
