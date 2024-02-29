/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.briefaction.display
import com.vladmarkovic.sample.shared_presentation.briefaction.navigate
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction.OpenDrawer
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction.Back

sealed class UpButton(
    open val icon: ImageVector,
    @StringRes open val contentDescriptionRes: Int,
    open val action: () -> Unit
) {

    data class CustomButton(
        override val icon: ImageVector,
        @StringRes override val contentDescriptionRes: Int,
        override val action: () -> Unit
    ) : UpButton(icon, contentDescriptionRes, action)

    data class BackButton(override val action: () -> Unit) :
        UpButton(Icons.AutoMirrored.Filled.ArrowBack, R.string.button_back_label, action) {
        companion object {
            operator fun <VM> invoke(vm: VM): BackButton where VM : BriefActionable, VM : ViewModel =
                BackButton { vm.navigate(Back) }
        }
    }

    data class DrawerButton(override val action: () -> Unit) :
        UpButton(Icons.Filled.Menu, R.string.button_open_drawer_label, action) {
        companion object {
            operator fun <VM> invoke(vm: VM): DrawerButton where VM : BriefActionable, VM : ViewModel =
                DrawerButton { vm.display(OpenDrawer) }
        }
    }
}
