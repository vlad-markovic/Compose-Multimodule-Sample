package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction

interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (BriefAction) -> Unit)
}