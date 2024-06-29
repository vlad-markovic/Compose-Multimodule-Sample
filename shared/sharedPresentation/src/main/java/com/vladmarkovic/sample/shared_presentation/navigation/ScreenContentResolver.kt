/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange


interface ScreenContentResolver {
    @Composable
    fun Screen.Content(
        scaffoldChangeHandler: (ScaffoldChange) -> Unit,
        bubbleUp: (BriefAction) -> Unit
    )
}
