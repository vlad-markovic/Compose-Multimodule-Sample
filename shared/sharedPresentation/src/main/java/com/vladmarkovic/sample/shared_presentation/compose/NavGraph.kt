/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.navigation.ScreenContentResolverEntryPoint
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route


@Composable
fun ScreensNavGraph(
    allScreens: List<Screen>,
    initialScreen: Screen = allScreens.first(),
    navController: NavHostController = rememberNavController(),
    actionHandler: (BriefAction) -> Unit = remember {{ throw IllegalStateException("Unhandled action: $it") }},
) {
    val screenContentResolver: ScreenContentResolver = remember {
        EntryPointAccessor.fromApplication(ScreenContentResolverEntryPoint::class.java).screenContentResolver()
    }
    NavScaffold(
        initialScreen = initialScreen,
        bottomBar = {},
        bubbleUp = actionHandler,
        navController = navController
    ) { modifier, scaffoldChangeHandler, bubbleUp ->
        NavHost(
            navController = navController,
            startDestination = initialScreen.name,
            modifier = modifier,
        ) {
            composeNavGraph(screenContentResolver, allScreens, scaffoldChangeHandler, bubbleUp)
        }
    }
}

/** Composes screens and a "navigation branch" with "composable" function for each screen. */
fun NavGraphBuilder.composeNavGraph(
    screenContentResolver: ScreenContentResolver,
    allScreens: List<Screen>,
    scaffoldChangeHandler: (ScaffoldChange) -> Unit,
    bubbleUp: (BriefAction) -> Unit
) {
    allScreens.forEach { screen ->
        composable(
            route = screen.route,
            arguments = screen.namedArgs,
        ) {
            with(screenContentResolver) {
                screen.Content(scaffoldChangeHandler, bubbleUp)
            }
        }
    }
}
