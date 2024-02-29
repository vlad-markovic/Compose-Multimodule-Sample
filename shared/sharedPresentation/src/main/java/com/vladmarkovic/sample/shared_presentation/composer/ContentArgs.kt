/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.composer

import android.app.Activity
import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("MemberVisibilityCanBePrivate")
data class ContentArgs(
    val type: ScreenHolderType,
    val navController: NavController,
    val scaffoldState: ScaffoldState,
    val mainScope: CoroutineScope,
    val backStackEntry: NavBackStackEntry
) {

    val context: Context get() = navController.context

    val isDrawerOpen: Boolean get() = scaffoldState.drawerState.isOpen

    val isInitialTabFirstScreen: Boolean
        get() = backStackEntry.isTabFirstScreen && navController.backQueue.size < type.initialBackstackSize

    fun closeDrawer() {
        mainScope.launch { scaffoldState.drawerState.close() }
    }

    fun openDrawer() {
        mainScope.launch { scaffoldState.drawerState.open() }
    }

    fun onBack() {
        when {
            isDrawerOpen -> closeDrawer()
            isInitialTabFirstScreen -> (navController.context as Activity).finish()
            else -> navController.popBackStack()
        }
    }

    private val NavBackStackEntry.isTabFirstScreen: Boolean
        get() = destination.parent?.startDestinationRoute == destination.route
}
