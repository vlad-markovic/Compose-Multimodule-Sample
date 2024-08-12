/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.viewaction.ActionViewModel
import com.vladmarkovic.sample.shared_presentation.viewaction.display
import com.vladmarkovic.sample.shared_presentation.viewaction.navigate
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction.Toast
import com.vladmarkovic.sample.shared_presentation.navigation.ToSettings
import com.vladmarkovic.sample.shared_domain.tab.MainBottomTab
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DrawerItem
import com.vladmarkovic.sample.shared_presentation.navigation.CloseDrawer
import com.vladmarkovic.sample.shared_presentation.screen.ToTab

sealed class MainDrawerItem(
    override val icon: ImageVector,
    @StringRes override val textRes: Int
) : DrawerItem {

    data class ItemPostsTab(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.AutoMirrored.Filled.List, R.string.bottom_tab_posts)

    data class ItemCovidTab(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Info, R.string.bottom_tab_covid)

    data class ItemToast(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Email, R.string.drawer_item_toast)

    data class ItemSettings(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Settings, R.string.drawer_item_settings)
}

fun defaultDrawerItems(viewModel: ActionViewModel) = listOf(
    MainDrawerItem.ItemPostsTab {
        viewModel.navigate(CloseDrawer)
        viewModel.navigate(ToTab(MainBottomTab.POSTS_TAB))
    },
    MainDrawerItem.ItemCovidTab {
        viewModel.navigate(CloseDrawer)
        viewModel.navigate(ToTab(MainBottomTab.COVID_TAB))
    },
    MainDrawerItem.ItemToast {
        viewModel.navigate(CloseDrawer)
        viewModel.display(Toast("A Toast"))
    },
    MainDrawerItem.ItemSettings {
        viewModel.navigate(CloseDrawer)
        viewModel.navigate(ToSettings)
    }
)
