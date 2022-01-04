/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.vladmarkovic.sample.shared_presentation.compose.BaseComposeHolder

/**
 * For decorating an Activity to give it Compose Navigation functionality,
 * extracted to provide composition over inheritance.
 */
@Suppress("FunctionName")
interface NavigableComposeHolder : BaseComposeHolder {

    val startDestination: String

    val initialGraphModifier: Modifier get() = Modifier

    fun NavGraphBuilder.navGraph(navController: NavHostController)

    @Composable
    override fun ScaffoldContent(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = initialGraphModifier
        ) {
            navGraph(navController)
        }
    }
}
