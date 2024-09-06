/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.mv.action.Actionable
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.action.navigate
import com.vladmarkovic.sample.common.navigation.screen.compose.R
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction.Back
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction.OpenDrawer

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
            operator fun <VM> invoke(vm: VM): BackButton where VM : ViewModel, VM : Actionable<Action> =
                BackButton { vm.navigate(Back) }
        }
    }

    data class DrawerButton(override val action: () -> Unit) :
        UpButton(Icons.Filled.Menu, R.string.button_open_drawer_label, action) {
        companion object {
            operator fun <VM> invoke(vm: VM): DrawerButton where VM : ViewModel, VM : Actionable<Action> =
                DrawerButton { vm.navigate(OpenDrawer) }
        }
    }
}
