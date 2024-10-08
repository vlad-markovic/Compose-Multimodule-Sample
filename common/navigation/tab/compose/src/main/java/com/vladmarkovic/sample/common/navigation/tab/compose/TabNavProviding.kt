/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.tab.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vladmarkovic.sample.common.di.compose.assistedViewModel
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.TabNavViewModel
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.TabNavViewModelFactory


@Composable
fun tabNavViewModel(initialTab: Tab<*>, navController: NavController, key: String? = null): TabNavViewModel =
    assistedViewModel<TabNavViewModel, Tab<*>, TabNavViewModelFactory>(
        assistedInput = initialTab,
        navBackStackEntry = navController.currentBackStackEntry,
        key = key
    )
