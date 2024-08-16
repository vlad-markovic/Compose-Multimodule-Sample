/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.compose.util.setComposeContentView
import com.vladmarkovic.sample.common.navigation.screen.compose.content.ComposeScreenContentResolver
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.routeData
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.DefaultScreensNavScaffold
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            DefaultScreensNavScaffold(
                allScreens = SettingsScreen.entries,
                routeDataResolver = remember {{ screen -> screen.routeData() }},
                contentResolver = hiltViewModel<SettingsScreenResolverProvider>().resolver,
            )
        }
    }
}


@HiltViewModel
class SettingsScreenResolverProvider @Inject constructor(
    val resolver: ComposeScreenContentResolver<SettingsScreen>
): ViewModel()
