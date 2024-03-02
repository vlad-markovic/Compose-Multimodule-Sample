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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigable
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.handleAction
import com.vladmarkovic.sample.shared_presentation.util.update
import kotlinx.coroutines.CoroutineScope

@Composable
fun ScreensHolder(
    topBar: @Composable (NavController, TopBarData?) -> Unit = { navController, topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    drawer: @Composable (ScaffoldState, CoroutineScope, DrawerData?) -> Unit = { scaffoldState, scope, topBarData ->
        DefaultDrawer(scaffoldState, scope, topBarData)
    },
    scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    ScaffoldHolder(topBar, drawer, bottomBar = {}, scaffoldContent)
}

@Composable
fun <S : Screen, T : Tab<S>> Tabs(
    tabNav: TabNavigable<S, T>,
    tabs: List<T>,
    topBar: @Composable (NavController, TopBarData?) -> Unit = { navController, topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    drawer: @Composable (ScaffoldState, CoroutineScope, DrawerData?) -> Unit = { scaffoldState, scope, topBarData ->
        DefaultDrawer(scaffoldState, scope, topBarData)
    },
    bottomBar: @Composable () -> Unit = {
        DefaultBottomBar(tabNav, tabs)
    },
    scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    ScaffoldHolder(topBar, drawer, bottomBar, scaffoldContent)
}

@Composable
private fun ScaffoldHolder(
    topBar: @Composable (NavController, TopBarData?) -> Unit = { navController, topBarData ->
        DefaultTopBar(navController, topBarData)
    },
    drawer: @Composable (ScaffoldState, CoroutineScope, DrawerData?) -> Unit = { scaffoldState, scope, topBarData ->
        DefaultDrawer(scaffoldState, scope, topBarData)
    },
    bottomBar: @Composable () -> Unit = {},
    scaffoldContent: @Composable (
        contentArgs: ContentArgs,
        modifier: Modifier,
        scaffoldChange: (ScaffoldChange) -> Unit,
        bubbleUp: (ScreenHolderType, BriefAction) -> Unit
    ) -> Unit
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val mainScope = rememberCoroutineScope()

    val screenData: MutableState<ScreenData?> = remember { mutableStateOf(null) }
    val topBarData: MutableState<TopBarData?> = remember { mutableStateOf(null) }
    val drawerData: MutableState<DrawerData?> = remember { mutableStateOf(null) }

    AppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBar(navController, topBarData.value) },
            bottomBar = bottomBar,
            drawerContent = { drawer(scaffoldState, mainScope, drawerData.value) }
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
                    contentArgs.handleAction(holderType, action)
                }
            )

            androidx.activity.compose.BackHandler {
                screenData.value?.let { contentArgs.handleAction(it.screenHolderType, CommonNavigationAction.Back) }
                    ?: navController.popBackStack()
            }
        }
    }
}
