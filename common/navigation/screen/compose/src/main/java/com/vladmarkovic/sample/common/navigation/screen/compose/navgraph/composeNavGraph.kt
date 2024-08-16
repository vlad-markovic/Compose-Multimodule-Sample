package com.vladmarkovic.sample.common.navigation.screen.compose.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun <S: Screen> NavGraphBuilder.composeNavGraph(
    allScreens: List<S>,
    bubbleUp: (ViewAction) -> Unit,
    routeDataMap: Map<S, ScreenRouteData>,
    contentResolver: ComposeScreenContentResolver<S>,
) {
    allScreens.forEach { screen ->
        val routeData = routeDataMap[screen]!!
        composable(
            route = routeData.routeWithPlaceholders,
            arguments = routeData.namedArgs,
            deepLinks = routeData.deepLinks,
        ) {
            with(contentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
