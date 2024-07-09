package com.vladmarkovic.sample.shared_presentation.compose

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.screen.deepLinks
import com.vladmarkovic.sample.shared_presentation.screen.enterTransition
import com.vladmarkovic.sample.shared_presentation.screen.exitTransition
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    screenContentResolver: ComposeScreenContentResolver,
    allScreens: List<NavGraphScreen>,
    bubbleUp: (BriefAction) -> Unit
) {
    allScreens.forEach { screen ->
        composable(
            route = screen.route,
            arguments = screen.namedArgs,
            deepLinks = screen.deepLinks,
            enterTransition = screen.enterTransition,
            exitTransition = screen.exitTransition,
        ) {
            with(screenContentResolver) {
                screen.Content(bubbleUp)
            }
        }
    }
}
