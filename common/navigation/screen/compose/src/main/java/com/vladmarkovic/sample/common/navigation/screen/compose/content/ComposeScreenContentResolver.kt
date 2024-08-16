package com.vladmarkovic.sample.common.navigation.screen.compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.mv.action.ViewAction

@Stable
interface ComposeScreenContentResolver {
    @Composable
    fun Screen.Content(bubbleUp: (ViewAction) -> Unit)
}
