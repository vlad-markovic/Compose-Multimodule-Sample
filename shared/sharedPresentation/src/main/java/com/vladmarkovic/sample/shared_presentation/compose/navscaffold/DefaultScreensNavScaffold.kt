package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.compose.animation.enterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.exitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popExitTransition
import com.vladmarkovic.sample.common.compose.util.lifecycleAwareValue
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.screen.ScreenRouteData

@Composable
fun DefaultScreensNavScaffold(
    allScreens: List<NavGraphScreen>,
    initialScreen: NavGraphScreen = allScreens.first(),
    bubbleUp: (ViewAction) -> Unit = rememberThrowingNoHandler(),
    routeDataResolver: (NavGraphScreen) -> ScreenRouteData
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialScreen)
    val scaffoldChangesHandler: (ViewAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, bubbleUp)

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
        bubbleUp = scaffoldChangesHandler,
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
        routeDataResolver = routeDataResolver
    )
}
