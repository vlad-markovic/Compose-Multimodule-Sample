/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.compose.util.setComposeContentView
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.routeData
import com.vladmarkovic.sample.shared_domain.screen.MainScreen
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.DefaultTabsNavScaffold
import com.vladmarkovic.sample.shared_presentation.project.extraArgsNames
import com.vladmarkovic.sample.shared_presentation.tab.icon
import com.vladmarkovic.sample.shared_presentation.tab.textRes
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            DefaultTabsNavScaffold(
                MainBottomTab.entries,
                routeDataResolver = remember {{ screen ->
                    screen.routeData(screen.extraArgsNames)
                }},
                tabDataResolver = remember {{ tab ->
                    tab.icon to tab.textRes
                }},
                contentResolver = hiltViewModel<MainScreenResolverProvider>().resolver
            )
        }
    }
}

@HiltViewModel
class MainScreenResolverProvider @Inject constructor(
    val resolver: ComposeScreenContentResolver<MainScreen>
) : ViewModel()
