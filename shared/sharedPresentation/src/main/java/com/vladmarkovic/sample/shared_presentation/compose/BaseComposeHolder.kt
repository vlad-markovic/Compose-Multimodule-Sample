/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme

/**
 * For decorating Activity, to provide base Compose Scaffold structure,
 * extracting to provide composition over inheritance.
 */
@Suppress("FunctionName")
interface BaseComposeHolder {

    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        AppTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { TopBar(scaffoldState, navController) },
            ) {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(Color.Black)

                ScaffoldContent(navController)
            }
        }
    }

    @Composable
    fun TopBar(scaffoldState: ScaffoldState, navController: NavHostController) {
        // No op
    }

    @Composable
    fun ScaffoldContent(navController: NavHostController)
}
