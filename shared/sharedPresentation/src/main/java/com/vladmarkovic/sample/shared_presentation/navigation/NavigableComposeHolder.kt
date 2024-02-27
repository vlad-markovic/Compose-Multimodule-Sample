/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.vladmarkovic.sample.shared_presentation.compose.BaseComposeHolder
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.CoroutineScope

/**
 * For decorating an Activity to give it Compose Navigation functionality,
 * extracted to provide composition over inheritance.
 */
interface NavigableComposeHolder<S : Screen> : BaseComposeHolder {

    val initialScreen: S

    val initialDestination: String get() = initialScreen.name

    val initialGraphModifier: Modifier get() = Modifier

    fun NavGraphBuilder.navGraph(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope
    )

    @Composable
    override fun ScaffoldContent(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope,
        paddingValues: PaddingValues
    ) {
        NavHost(
            navController = navController,
            startDestination = initialDestination,
            modifier = Modifier.padding(paddingValues).then(initialGraphModifier)
        ) {
            navGraph(navController, scaffoldState, mainScope)
        }
    }
}
