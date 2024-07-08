package com.vladmarkovic.sample.shared_presentation.compose

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.screen.deepLinks
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    screenContentResolver: ScreenContentResolver,
    allScreens: List<Screen>,
    bubbleUp: (BriefAction) -> Unit
) {
    allScreens.forEach { screen ->
        composable(
            route = screen.route,
            arguments = screen.namedArgs,
            deepLinks = screen.deepLinks
        ) {
            with(screenContentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
