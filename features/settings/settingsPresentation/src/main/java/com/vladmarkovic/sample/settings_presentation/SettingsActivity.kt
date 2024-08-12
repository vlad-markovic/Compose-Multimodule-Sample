/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.settings_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_domain.screen.SettingsScreen
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.DefaultScreensNavScaffold
import com.vladmarkovic.sample.common.compose.util.setComposeContentView
import com.vladmarkovic.sample.shared_presentation.screen.routeData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            DefaultScreensNavScaffold(SettingsScreen.entries) { it.routeData() }
        }
    }
}
