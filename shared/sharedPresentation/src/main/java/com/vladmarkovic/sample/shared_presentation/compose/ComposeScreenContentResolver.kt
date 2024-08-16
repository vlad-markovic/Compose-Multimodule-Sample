package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.view.action.ViewAction

@Stable
interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (ViewAction) -> Unit)
}
