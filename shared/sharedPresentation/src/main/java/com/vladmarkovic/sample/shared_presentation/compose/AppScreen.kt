package com.vladmarkovic.sample.shared_presentation.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton.BackButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme

@Composable
fun AppScreen(
    title: String,
    up: State<UpButton>? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = { TopBar(title, up = up) },
        ) { paddingValues ->
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Black)

            if (up?.value is BackButton) {
                BackHandler {
                    up.value.action()
                }
            }

            content(paddingValues)
        }
    }
}

@Composable
fun TopBar(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    up: State<UpButton>? = null
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = AppColor.Grey900,
        navigationIcon = { up?.let { Up(it) } },
        title = {
            Text(
                modifier = modifier.then(Modifier.fillMaxWidth()),
                text = text,
                textAlign = textAlign,
                style = AppTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    )
}

@Composable
fun Up(_upButton: State<UpButton>) {
    val upButton by _upButton
    IconButton(upButton.action) {
        Icon(upButton.icon, contentDescription = stringResource(upButton.contentDescriptionRes))
    }
}

@Preview
@Composable
private fun PreviewTopBar() {
    TopBar("Top Title")
}
