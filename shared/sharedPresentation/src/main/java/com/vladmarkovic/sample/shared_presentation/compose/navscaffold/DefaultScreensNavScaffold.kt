package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.animation.enterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.exitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.popExitTransition
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultDrawer
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.compose.safeValue
import com.vladmarkovic.sample.shared_presentation.screen.ScreenRouteData
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor

@Composable
fun DefaultScreensNavScaffold(
    allScreens: List<NavGraphScreen>,
    initialScreen: NavGraphScreen = allScreens.first(),
    bubbleUp: (BriefAction) -> Unit = rememberThrowingNoHandler(),
    routeDataResolver: (NavGraphScreen) -> ScreenRouteData
) {
    val scaffoldData: ScaffoldDataManager = rememberScaffoldDataManager(initialScreen)
    val scaffoldChangesHandler: (BriefAction) -> Unit = rememberScaffoldChangesHandler(scaffoldData, bubbleUp)

    val drawerData = scaffoldData.drawerData.safeValue

    ScreensNavScaffold(
        allScreens = allScreens,
        initialScreen = initialScreen,
        topBar = {
            val topBarData = scaffoldData.topBarData.safeValue
            if (topBarData != null) DefaultTopBar(topBarData) else Unit
        },
        drawerContent = remember(drawerData) { drawerData?.drawerItems?.let {{ DefaultDrawer(it) }} },
        bubbleUp = scaffoldChangesHandler,
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
        routeDataResolver = routeDataResolver
    )
}
