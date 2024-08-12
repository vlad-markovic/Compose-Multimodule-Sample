package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_domain.screen.NavGraphScreen

@Stable
interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (ViewAction) -> Unit)
}
