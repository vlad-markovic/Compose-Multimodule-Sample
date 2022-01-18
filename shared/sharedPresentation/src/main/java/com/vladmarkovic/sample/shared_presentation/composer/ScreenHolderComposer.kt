/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.namedArgs
import com.vladmarkovic.sample.shared_presentation.screen.route
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * Holds a number of scoped screens specifying all it holds, and serves as a screen selector.
 * Also saves and holds the state for the current screen.
 */
@Suppress("FunctionName")
interface ScreenHolderComposer<S: Screen> {

    val type: ScreenHolderType

    /** Override providing values() of the scoped screens enum */
    val allScreens: Array<S>

    /** Set to first screen to be initial screen */
    val currentScreen: MutableState<S>

    /**
     * Inject [ScreenComposer]s for each screen, and override to provide
     * with when statement a composer for each screen.
     */
    fun composer(screen: S): ScreenComposer

    /** Composes screens and a "navigation branch" with "composable" function for each screen. */
    fun NavGraphBuilder.composeNavGraph(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope
    ) {
        allScreens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.namedArgs,
            ) { backStackEntry ->
                val contentArgs = ContentArgs(type, navController, scaffoldState, mainScope, backStackEntry)

                BackHandler(contentArgs)

                currentScreen.value = screen

                with(composer(screen)) {
                    Content(contentArgs)
                }
            }
        }
    }

    @Composable
    fun ComposeTopBar(navController: NavHostController) {
        val screen by currentScreen
        composer(screen).TopBar(navController)
    }

    @Composable
    fun ComposeDrawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        val screen by currentScreen
        composer(screen).Drawer(scaffoldState, mainScope)
    }

    @Composable
    fun BackHandler(contentArgs: ContentArgs) {
        contentArgs.navController.enableOnBackPressed(false)
        val actionViewModel = actionViewModel<BriefActionViewModel>(contentArgs)
        androidx.activity.compose.BackHandler { actionViewModel.navigate(Back) }
    }
}
