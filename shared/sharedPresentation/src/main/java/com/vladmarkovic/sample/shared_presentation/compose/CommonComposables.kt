package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.R
import com.vladmarkovic.sample.shared_presentation.ui.compose.TopBar
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens

@Composable
fun AppScreen(
    title: String,
    content: @Composable (PaddingValues) -> Unit,
) {
    AppTheme {
        Scaffold(
            topBar = { TopBar(title) },
        ) { paddingValues ->
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Black)

            content(paddingValues)
        }
    }
}

@Composable
fun Error(error: String, onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m * 2)
    ) {
        Text(
            modifier = Modifier.padding(end = Dimens.m).align(Alignment.CenterVertically),
            text = error
        )
        Button(onRetry) {
            Text(stringResource(R.string.button_retry_label))
        }
    }
}
