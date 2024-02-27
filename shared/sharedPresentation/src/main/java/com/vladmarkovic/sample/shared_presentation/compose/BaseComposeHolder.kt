/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope

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
        val mainScope = rememberCoroutineScope()

        AppTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { TopBar(navController) },
                bottomBar = { BottomBar() },
                drawerContent = { Drawer(scaffoldState, mainScope) }
            ) { paddingValues ->
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(Color.Black)

                ScaffoldContent(navController, scaffoldState, mainScope, paddingValues)
            }
        }
    }

    @Composable
    fun TopBar(navController: NavHostController) {
        // No op
    }

    @Composable
    fun BottomBar() {
        // No op
    }

    @Composable
    fun Drawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope) {
        // No op
    }

    @Composable
    fun ScaffoldContent(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        mainScope: CoroutineScope,
        paddingValues: PaddingValues
    )
}
