package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.action.rememberThrowingNoHandler
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.ScreensNavScaffold
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData
import com.vladmarkovic.sample.shared_presentation.compose.animation.enterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.exitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popExitTransition
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.rememberTopActionHandler

@Composable
fun <S : Screen> DefaultScreensNavScaffold(
    allScreens: List<S>,
    initialScreen: S = allScreens.first(),
    bubbleUp: (ViewAction) -> Unit = rememberThrowingNoHandler(),
    routeDataResolver: (S) -> ScreenRouteData,
    contentResolver: ComposeScreenContentResolver<S>,
) {
    val (scaffoldData, topHandler) = rememberTopActionHandler(initialScreen, bubbleUp)

    AppTheme {
        ScreensNavScaffold(
            allScreens = allScreens,
            initialScreen = initialScreen,
            topBar = {
                val topBarData = scaffoldData.topBarData.lifecycleAwareValue
                if (topBarData != null) DefaultTopBar(topBarData) else Unit
            },
            drawerContent = {
                val drawerData = scaffoldData.drawerData.lifecycleAwareValue
                drawerData?.drawerItems?.let { DefaultDrawer(it) }
            },
            bubbleUp = topHandler,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() },
            routeDataResolver = routeDataResolver,
            contentResolver = contentResolver,
        )
    }
}
