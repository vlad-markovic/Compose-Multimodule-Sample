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
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.util.setupWith
import kotlinx.coroutines.CoroutineScope

@Composable
fun TestCompose(
    tab: Tab<*>,
    viewModel: BriefActionViewModel,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mainScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (ContentArgs) -> Unit
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
            ) { backStackEntry ->
                val contentArgs = ContentArgs(ScreenHolderType.STANDALONE, navController,
                    scaffoldState, mainScope, backStackEntry)

                viewModel.apply { actioner.setupWith(contentArgs) }

                content(contentArgs)
            }
        }
    }
}