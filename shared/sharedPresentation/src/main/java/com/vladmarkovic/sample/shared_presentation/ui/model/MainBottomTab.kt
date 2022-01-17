package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen

@Suppress("UNCHECKED_CAST")
enum class MainBottomTab(
    override val screens: Array<MainScreen>,
    override val icon: ImageVector,
    @StringRes override val textRes: Int,
    override val initialScreen: MainScreen = screens.first()
) : Tab<MainScreen> {
    POSTS_TAB(
        MainScreen.PostsScreen.values() as Array<MainScreen>,
        Icons.Filled.List,
        R.string.bottom_tab_posts
    )
}
