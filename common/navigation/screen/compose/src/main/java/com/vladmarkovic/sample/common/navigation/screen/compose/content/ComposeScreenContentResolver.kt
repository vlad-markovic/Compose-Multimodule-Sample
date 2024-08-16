package com.vladmarkovic.sample.common.navigation.screen.compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.common.navigation.screen.model.NavGraphScreen
import com.vladmarkovic.sample.common.mv.action.ViewAction

@Stable
interface ComposeScreenContentResolver {
    @Composable
    fun NavGraphScreen.Content(bubbleUp: (ViewAction) -> Unit)
}
