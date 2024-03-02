/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.drawer

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.Serializable

@Stable
interface DrawerItem : Serializable {
    val icon: ImageVector
    val textRes: Int
    val onClick: () -> Unit
}
