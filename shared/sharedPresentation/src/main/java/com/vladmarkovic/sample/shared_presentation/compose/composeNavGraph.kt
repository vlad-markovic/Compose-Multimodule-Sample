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
            route = routeData.route,
            arguments = routeData.namedArgs,
            deepLinks = routeData.deepLinks,
            // These animate non-tab screens (Settings 1 <-> 2), and tab change only on first tab screens,
            // but don't animate tab screens or tab changes if not on first screen.
            // Using either here or for NavHost is the same.
            enterTransition = routeData.transitions.enter?.let {{ it(screen) }},
            exitTransition = routeData.transitions.exit?.let {{ it(screen) }},
            popEnterTransition = routeData.transitions.popEnter?.let {{ it(screen) }},
            popExitTransition = routeData.transitions.popExit?.let {{ it(screen) }},
        ) {
            with(screenContentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
