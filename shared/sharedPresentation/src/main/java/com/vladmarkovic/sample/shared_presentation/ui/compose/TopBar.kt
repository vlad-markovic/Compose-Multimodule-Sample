package com.vladmarkovic.sample.shared_presentation.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme

@Composable
fun TopBar(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = AppColor.Grey900
    ) {
        Text(
            modifier = modifier.then(Modifier.fillMaxWidth()),
            text = text,
            textAlign = textAlign,
            style = AppTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
