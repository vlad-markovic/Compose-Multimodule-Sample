package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen
import com.vladmarkovic.sample.shared_presentation.viewaction.ViewAction

@Stable
interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (ViewAction) -> Unit)
}
