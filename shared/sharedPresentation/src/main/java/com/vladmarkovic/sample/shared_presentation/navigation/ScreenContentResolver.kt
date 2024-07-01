/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction


interface ScreenContentResolver {
    @Composable
    fun Screen.Content(bubbleUp: (BriefAction) -> Unit)
}
