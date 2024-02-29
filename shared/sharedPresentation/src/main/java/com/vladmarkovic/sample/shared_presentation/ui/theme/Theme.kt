/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.font.FontWeight

object AppTheme {
    val shapes = Shapes(
        small = RoundedCornerShape(Dimens.m / 4),
        medium = RoundedCornerShape(Dimens.m / 2),
        large = RoundedCornerShape(Dimens.m)
    )

    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.copy(
            h5 = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
        )
}

private val DarkColorPalette = darkColors(
    primary = AppColor.Grey600,
    primaryVariant = AppColor.Grey900,
    secondary = AppColor.Teal200
)

private val LightColorPalette = lightColors(
    primary = AppColor.Grey700,
    primaryVariant = AppColor.Grey900,
    secondary = AppColor.Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
@Suppress("FunctionName")
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = AppTheme.typography,
        shapes = AppTheme.shapes,
        content = content
    )
}