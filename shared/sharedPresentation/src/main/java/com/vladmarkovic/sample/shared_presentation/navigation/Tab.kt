/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction.NavigationAction
import com.vladmarkovic.sample.shared_presentation.screen.Screen

interface Tab<S: Screen> : NavigationAction {
    val name: String
    val initialScreen: S
    val screens: List<S>
    val icon: ImageVector
    val textRes: Int
}
