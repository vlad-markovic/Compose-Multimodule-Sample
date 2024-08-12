/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_android_test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.common.view.action.ActionViewModel
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.compose.di.SetupWith
import com.vladmarkovic.sample.shared_presentation.tab.route

@Composable
fun TestCompose(
    tab: Tab,
    viewModel: ActionViewModel,
    scaffoldContent: @Composable (navController: NavHostController, modifier: Modifier, bubbleUp: (ViewAction) -> Unit) -> Unit
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = tab.route,
        modifier = Modifier
    ) {
        navigation(
            startDestination = tab.initialScreen.name,
            route = tab.route
        ) {
            composable(
                route = tab.initialScreen.name,
                arguments = emptyList(),
            ) {
                val actionHandler: (ViewAction) -> Unit = {
                    println("Bubbled up action: $it")
                }

                viewModel.SetupWith(actionHandler)

                scaffoldContent(navController, Modifier, actionHandler)
            }
        }
    }
}
