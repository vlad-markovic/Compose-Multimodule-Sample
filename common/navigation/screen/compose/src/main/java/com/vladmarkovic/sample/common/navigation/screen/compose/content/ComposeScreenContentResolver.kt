package com.vladmarkovic.sample.common.navigation.screen.compose.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.model.Screen

@Stable
interface ComposeScreenContentResolver<S : Screen> {

    @Composable
    fun S.Content(bubbleUp: (Action) -> Unit)
}
