/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.drawer

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction.Toast
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes

sealed class MainDrawerItem(override val icon: ImageVector,
                            @StringRes override val textRes: Int) : DrawerItem {

    data class ItemToast(override val onClick: () -> Unit) :
        MainDrawerItem(Icons.Filled.Email, R.string.drawer_item_toast)
}

fun defaultDrawerItems(viewModel: BriefActionViewModel) = listOf(
    MainDrawerItem.ItemToast { viewModel.display(Toast(StrOrRes.str("Show Toast"))) },
)
