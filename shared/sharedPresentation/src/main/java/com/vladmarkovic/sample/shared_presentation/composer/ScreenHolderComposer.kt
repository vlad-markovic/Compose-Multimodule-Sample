/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_presentation.compose.OnStart
import com.vladmarkovic.sample.shared_presentation.compose.ScreenHolderChange
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route

/**
 * Holds a number of scoped screens specifying all it holds, and serves as a screen selector.
 * Also saves and holds the state for the current screen.
 */
interface ScreenHolderComposer<S: Screen> : CurrentScreenMonitor<S> {

    val type: ScreenHolderType

    /**
     * Inject [ScreenComposer]s for each screen, and override to provide
     * with when statement a composer for each screen.
     */
    fun composer(screen: S): ScreenComposer<*>

    /** Composes screens and a "navigation branch" with "composable" function for each screen. */
    fun NavGraphBuilder.composeNavGraph(args: ScreenArgs) {
        allScreens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.namedArgs,
            ) { _ ->
//                updateCurrentScreen(screen) TODO?
                with(composer(screen)) {
                    Content(args) {
                        OnStart { args.bubbleUp(ScreenHolderChange(type)) }
                    }
                }
            }
        }
    }
}
