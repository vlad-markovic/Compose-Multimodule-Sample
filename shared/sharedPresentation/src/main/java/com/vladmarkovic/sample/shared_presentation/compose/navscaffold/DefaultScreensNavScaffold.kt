package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.animation.slideEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.slideExitTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.slidePopEnterTransition
import com.vladmarkovic.sample.shared_presentation.compose.animation.slidePopExitTransition
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
        topBar = remember {{
            DefaultTopBar(
                data = scaffoldData.topBarData.safeValue,
                backgroundColor = AppColor.Grey900
            )
        }},
        drawerContent = remember(drawerData) { drawerData?.drawerItems?.let {{ DefaultDrawer(it) }} },
        bubbleUp = scaffoldChangesHandler,
        enterTransition = { slideEnterTransition() },
        exitTransition = { slideExitTransition() },
        popEnterTransition = { slidePopEnterTransition() },
        popExitTransition = { slidePopExitTransition() },
        routeDataResolver = routeDataResolver
    )
}
