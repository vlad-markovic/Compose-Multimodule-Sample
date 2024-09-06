/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_android_test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.common.mv.action.compose.SetupWith
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.util.route

@Composable
fun TestCompose(
    tab: Tab<*>,
    viewModel: ActionViewModel,
    scaffoldContent: @Composable (navController: NavHostController, modifier: Modifier, bubbleUp: (Action) -> Unit) -> Unit
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
                val actionHandler: (Action) -> Unit = {
                    println("Bubbled up action: $it")
                }

                viewModel.SetupWith(actionHandler)

                scaffoldContent(navController, Modifier, actionHandler)
            }
        }
    }
}
