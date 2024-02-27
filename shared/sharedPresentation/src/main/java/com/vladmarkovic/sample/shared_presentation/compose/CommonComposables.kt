/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.shared_presentation.R.string.button_retry_label
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens

@Composable
fun Error(error: String, onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m * 2)
    ) {
        Text(
            modifier = Modifier
                .padding(end = Dimens.m)
                .align(Alignment.CenterVertically),
            text = error
        )
        Button(onRetry) {
            Text(stringResource(button_retry_label))
        }
    }
}

@Composable
fun AnimateSlide(
    visible: Boolean,
    reverse: Int = 1,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { -1 * reverse * it / 4 },
        exit = slideOutHorizontally { reverse * it / 4 },
        content = content
    )
}

@Composable
fun AnimateFade(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = content
    )
}
