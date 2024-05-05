/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.NavScaffold
import com.vladmarkovic.sample.shared_presentation.di.ProviderViewModel
import com.vladmarkovic.sample.shared_presentation.di.inject
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route

/** Specifies a list of scoped screens, and serves as a screen composer selector. */
interface ScreenComposerSelector<S: Screen> {

    val allScreens: List<S>
    val initialScreen: S get() = allScreens.first()

    /** Override to select, with when statement, a [ScreenComposer] for each screen (injected). */
    val S.screenComposer: ScreenComposer<*>
}


@Composable
inline fun <reified S : ScreenComposerSelector<*>, reified P : ProviderViewModel<S>> ScreensNavGraph(
    navController: NavHostController = rememberNavController(),
    noinline actionHandler: (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }}
) {
    val screenComposerSelector = inject<S, P>()
    NavScaffold(
        initialScreen = screenComposerSelector.initialScreen,
        bottomBar = {},
        bubbleUp = actionHandler,
        navController = navController
    ) { modifier, bubbleUp ->
        NavHost(
            navController = navController,
            startDestination = screenComposerSelector.initialScreen.name,
            modifier = modifier,
        ) {
            composeNavGraph(screenComposerSelector, bubbleUp)
        }
    }
}


/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun <S: Screen> NavGraphBuilder.composeNavGraph(
    screenComposerSelector: ScreenComposerSelector<S>,
    bubbleUp: (BriefAction) -> Unit
) {
    with(screenComposerSelector) {
        allScreens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.namedArgs,
            ) {
                with(screen.screenComposer) {
                    Content(bubbleUp)
                }
            }
        }
    }
}
