package com.vladmarkovic.sample.common.navigation.screen.compose.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.content.injectScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    allScreens: List<Screen>,
    bubbleUp: (ViewAction) -> Unit,
    routeDataMap: Map<Screen, ScreenRouteData>,
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
