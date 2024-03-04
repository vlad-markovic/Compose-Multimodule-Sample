/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_android_test

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.composer.ComposeArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.util.SetupWith
import kotlinx.coroutines.CoroutineScope

@Composable
fun TestCompose(
    tab: Tab<*>,
    viewModel: BriefActionViewModel,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (ScreenArgs) -> Unit
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
            ) { _ ->
                val composeArgs = ComposeArgs(navController, scaffoldState, mainScope)
                val actionHandler: (BriefAction) -> Unit = {

                }
                val screenArgs = ScreenArgs(composeArgs, actionHandler)

                viewModel.SetupWith(actionHandler)

                content(screenArgs)
            }
        }
    }
}