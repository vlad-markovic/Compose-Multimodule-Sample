/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import androidx.compose.runtime.*
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.screen.route
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel

/**
 * Holds a number of scoped screens specifying all it holds, and serves as a screen selector.
 * Also saves and holds the state for the current screen.
 */
@Suppress("FunctionName")
abstract class ScreenHolderComposer<S: Screen> {

    /** Override providing values() of the scoped screens enum */
    abstract val allScreens: Array<S>

    /** Set to first screen to be initial screen */
    protected open val screen: MutableState<S> by lazy { mutableStateOf(allScreens.first()) }
    val currentScreen: State<S> by lazy { screen }

    /**
     * Inject [ScreenComposer]s for each screen, and override to provide
     * with when statement a composer for each screen.
     */
    abstract fun composer(screen: S): ScreenComposer

    /** Composes screens and a "navigation branch" with "composable" function for each screen. */
    fun NavGraphBuilder.composeNavGraph(navController: NavHostController) {
        allScreens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.args ?: emptyList(),
            ) {
                BackHandler(navController)

                this@ScreenHolderComposer.screen.value = screen

                with(composer(screen)) {
                    Content(navController)
                }
            }
        }
    }

    @Composable
    fun ComposeTopBar(navController: NavHostController) {
        val screen by this@ScreenHolderComposer.screen
        composer(screen).TopBar(navController)
    }

    @Composable
    fun BackHandler(navController: NavHostController) {
        navController.enableOnBackPressed(false)
        val actionViewModel = actionViewModel<BriefActionViewModel>(navController)
        androidx.activity.compose.BackHandler { actionViewModel.navigate(Back) }
    }
}
