/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.DefaultTabsNavScaffold
import com.vladmarkovic.sample.shared_presentation.util.setComposeContentView
import dagger.hilt.android.AndroidEntryPoint

/** Main holder activity, holding composers for Composable-s. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setComposeContentView {
            DefaultTabsNavScaffold(MainBottomTab.entries)
        }
    }
}

