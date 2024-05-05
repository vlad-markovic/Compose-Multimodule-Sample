/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.screen.Screen

interface Tab : NavigationAction {
    val name: String
    val screens: List<Screen>
    val initialScreen: Screen get() = screens.first()
    val icon: ImageVector
    val textRes: Int
}
