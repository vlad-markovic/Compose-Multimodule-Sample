/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_domain.screen.MainScreen

enum class MainBottomTab(
    override val screens: List<MainScreen>,
    override val icon: ImageVector,
    @StringRes override val textRes: Int
) : Tab {
    POSTS_TAB(
        MainScreen.PostsScreen.entries as List<MainScreen>,
        Icons.AutoMirrored.Filled.List,
        R.string.bottom_tab_posts
    ),
    COVID_TAB(
        MainScreen.CovidScreen.entries as List<MainScreen>,
        Icons.Filled.Info,
        R.string.bottom_tab_covid
    )
}
