/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.drawer

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction.Toast
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.SettingsScreen.ToSettings
import com.vladmarkovic.sample.shared_presentation.ui.model.MainBottomTab

sealed class MainDrawerItem(override val icon: ImageVector,
                            @StringRes override val textRes: Int) : DrawerItem {

    data class ItemPostsTab(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.List, R.string.bottom_tab_posts)

    data class ItemCovidTab(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Info, R.string.bottom_tab_covid)

    data class ItemToast(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Email, R.string.drawer_item_toast)

    data class ItemSettings(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Settings, R.string.drawer_item_settings)
}

fun defaultDrawerItems(viewModel: BriefActionViewModel) = listOf(
    MainDrawerItem.ItemPostsTab { viewModel.navigate(MainBottomTab.POSTS_TAB) },
    MainDrawerItem.ItemCovidTab { viewModel.navigate(MainBottomTab.COVID_TAB) },
    MainDrawerItem.ItemToast { viewModel.display(Toast("A Toast")) },
    MainDrawerItem.ItemSettings { viewModel.navigate(ToSettings) }
)
