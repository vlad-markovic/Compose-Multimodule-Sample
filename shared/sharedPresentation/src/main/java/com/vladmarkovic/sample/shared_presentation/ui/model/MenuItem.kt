/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.model

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector

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
        val checked: State<Boolean>
        val onCheckedChange: (Boolean) -> Unit
    }
}
