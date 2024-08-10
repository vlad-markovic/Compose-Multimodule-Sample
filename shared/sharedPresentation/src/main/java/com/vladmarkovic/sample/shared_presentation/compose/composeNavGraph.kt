package com.vladmarkovic.sample.shared_presentation.compose

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.screen.ScreenRouteData

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    screenContentResolver: ComposeScreenContentResolver,
    allScreens: List<NavGraphScreen>,
    bubbleUp: (BriefAction) -> Unit,
    routeDataResolver: (NavGraphScreen) -> ScreenRouteData
) {
    allScreens.forEach { screen ->
        val routeData = routeDataResolver(screen)
        composable(
            route = routeData.routeWithPlaceholders,
            arguments = routeData.namedArgs,
            deepLinks = routeData.deepLinks,
        ) {
            with(screenContentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
