package com.vladmarkovic.sample.shared_presentation.compose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.shared_presentation.R
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
            Text(stringResource(R.string.button_retry_label))
        }
    }
}

@Composable
fun LogComposition(what: String) {
    val s = remember {
        val str = "a"
        Lumber.e("COMPOSE $what, str:${str.hashCode()}"); str
    }
    DisposableEffect(Unit) {
        onDispose { Lumber.e("DECOMPOSE $what, str:${s.hashCode()}") }
    }
    Lumber.e("RECOMPOSE $what, str:${s.hashCode()}")
}
