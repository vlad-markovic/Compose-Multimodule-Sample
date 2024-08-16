/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.StateFlow

/** Defines all elements needed to represent different types of menu items. */
sealed interface MenuItem {

    val icon: ImageVector? get() = null
    val showAlways: Boolean get() = icon != null

    interface Simple : MenuItem {
        val textRes: Int
        val colorRes: Int? get() = null
        val onClick: () -> Unit
    }

    interface Toggle : MenuItem {
        val checkedTextRes: Int
        val uncheckedTextRes: Int
        val checkedColorRes: Int? get() = null
        val uncheckedColorRes: Int? get() = null
        val checked: StateFlow<Boolean>
        val onCheckedChange: (Boolean) -> Unit
    }
}
