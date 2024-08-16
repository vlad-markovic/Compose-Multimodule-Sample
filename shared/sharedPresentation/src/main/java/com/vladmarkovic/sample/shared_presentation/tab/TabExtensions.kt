package com.vladmarkovic.sample.shared_presentation.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.R

val MainBottomTab.icon: ImageVector get() = when (this) {
    MainBottomTab.POSTS_TAB -> Icons.AutoMirrored.Filled.List
    MainBottomTab.COVID_TAB -> Icons.Filled.Info
}

val MainBottomTab.textRes: Int get() = when (this) {
    MainBottomTab.POSTS_TAB -> R.string.bottom_tab_posts
    MainBottomTab.COVID_TAB -> R.string.bottom_tab_covid
}
