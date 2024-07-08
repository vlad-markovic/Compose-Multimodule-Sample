package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction

interface ComposeScreenContentResolver {
    @Composable
    fun Screen.Content(bubbleUp: (BriefAction) -> Unit)
}