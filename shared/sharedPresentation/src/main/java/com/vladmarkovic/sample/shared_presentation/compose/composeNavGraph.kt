package com.vladmarkovic.sample.shared_presentation.compose

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.injectScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.screen.ScreenRouteData

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    allScreens: List<NavGraphScreen>,
    bubbleUp: (ViewAction) -> Unit,
    routeDataMap: Map<NavGraphScreen, ScreenRouteData>,
) {
    allScreens.forEach { screen ->
        val routeData = routeDataMap[screen]!!
        composable(
            route = routeData.routeWithPlaceholders,
            arguments = routeData.namedArgs,
            deepLinks = routeData.deepLinks,
        ) {
            val screenContentResolver: ComposeScreenContentResolver = injectScreenContentResolver()
            with(screenContentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
