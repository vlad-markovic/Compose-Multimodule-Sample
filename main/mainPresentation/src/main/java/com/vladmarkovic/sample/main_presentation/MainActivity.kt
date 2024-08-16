/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.DefaultTabsNavScaffold
import com.vladmarkovic.sample.common.compose.util.setComposeContentView
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.project.extraArgsNames
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ScreenRouteData
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.routeData
import dagger.hilt.android.AndroidEntryPoint

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            val routeDataResolver: (NavGraphScreen) -> ScreenRouteData = remember {{ screen ->
                screen.routeData(screen.extraArgsNames)
            }}
            DefaultTabsNavScaffold(MainBottomTab.entries, routeDataResolver = routeDataResolver)
        }
    }
}
