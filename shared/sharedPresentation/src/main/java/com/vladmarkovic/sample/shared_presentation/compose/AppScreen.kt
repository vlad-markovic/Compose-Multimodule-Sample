/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme

@Composable
fun AppScreen(
    title: State<StrOrRes>,
    up: State<UpButton?>? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = { DefaultTopBar(title, up = up) },
        ) { paddingValues ->
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Black)

            (up?.value as? BackButton)?.let { backButton ->
                BackHandler {
                    backButton.action()
                }
            }

            content(paddingValues)
        }
    }
}

@Composable
fun DefaultTopBar(
    _title: State<StrOrRes>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    up: State<UpButton?>? = null
) {
    val title by _title

    TopAppBar(
        modifier = modifier,
        backgroundColor = AppColor.Grey900,
        navigationIcon = { up?.let { Up(it) } },
        title = {
            Text(
                modifier = modifier.then(Modifier.fillMaxWidth()),
                text = title.get(LocalContext.current),
                textAlign = textAlign,
                style = AppTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    )
}

@Composable
fun Up(_upButton: State<UpButton?>) {
    val upButton by _upButton
    upButton?.let {
        IconButton(it.action) {
            Icon(it.icon, contentDescription = stringResource(it.contentDescriptionRes))
        }
    }
}

@Preview
@Composable
private fun PreviewTopBar() {
    DefaultTopBar(remember { mutableStateOf(StrOrRes.str("Top Title")) })
}
