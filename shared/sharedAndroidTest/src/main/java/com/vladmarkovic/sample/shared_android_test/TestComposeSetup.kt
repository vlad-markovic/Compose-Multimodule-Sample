/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_android_test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.util.SetupWith

@Composable
fun TestCompose(
    tab: Tab<*>,
    viewModel: BriefActionViewModel,
    navController: NavHostController = rememberNavController(),
    scaffoldContent: @Composable (navController: NavHostController, modifier: Modifier, bubbleUp: (BriefAction) -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = tab.name,
        modifier = Modifier
    ) {
        navigation(
            startDestination = tab.initialScreen.name,
            route = tab.name
        ) {
            composable(
                route = tab.initialScreen.name,
                arguments = emptyList(),
            ) {
                val actionHandler: (BriefAction) -> Unit = {
                    println("Bubbled up action: $it")
                }

                viewModel.SetupWith(actionHandler)

                scaffoldContent(navController, Modifier, actionHandler)
            }
        }
    }
}