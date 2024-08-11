/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.vladmarkovic.sample.shared_presentation.R.string
import com.vladmarkovic.sample.shared_presentation.compose.safeValue
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme


@Composable
fun DefaultTopBar(
    data: TopBarData?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppColor.Grey900,
    textAlign: TextAlign = TextAlign.Start,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
) {
    if (data != null) {
        TopAppBar(
            modifier = modifier.then(Modifier.fillMaxWidth()),
            backgroundColor = backgroundColor,
            navigationIcon = { data.upButton?.let { UpButton(it) } },
            actions = { data.menuItems?.let { DefaultMenu(it) } },
            elevation = elevation,
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.title?.get(LocalContext.current).orEmpty(),
                    textAlign = textAlign,
                    style = AppTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        )
    }
}

@Composable
fun UpButton(upButton: UpButton?) {
    upButton?.let {
        IconButton(it.action) {
            Icon(
                imageVector = it.icon,
                contentDescription = stringResource(it.contentDescriptionRes),
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTopBar() {
    DefaultTopBar(TopBarData(StrOrRes.str("Top Title")))
}

@Composable
fun DefaultMenu(menuItems: List<MenuItem>) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .semantics { contentDescription = "Menu" }
    ) {
        menuItems.filter { it.showAlways }.forEach { menuItem ->
            when (menuItem) {
                is MenuItem.Simple -> {
                    IconButton(menuItem.onClick) {
                        Icon(
                            imageVector = menuItem.icon!!,
                            contentDescription = stringResource(menuItem.textRes),
                            tint = menuItem.colorRes?.let { colorResource(it) } ?: defaultColor()
                        )
                    }
                }
                is MenuItem.Toggle -> {
                    val checked = remember { mutableStateOf(true) }

                    IconToggleButton(
                        checked = checked.value,
                        onCheckedChange = menuItem.onCheckedChange
                    ) {
                        val tint by animateColorAsState(menuItem.color(checked.value), label = "animate_appbar_icon_color")
                        Icon(
                            Filled.Favorite,
                            contentDescription = menuItem.text(checked.value),
                            tint = tint
                        )
                    }
                }
            }
        }

        IconButton({ expanded.value = true }) {
            Icon(
                Filled.ArrowDropDown,
                contentDescription = stringResource(string.button_show_menu_label),
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            menuItems.forEachIndexed { index, menuItem ->
                when (menuItem) {
                    is MenuItem.Simple -> {
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            menuItem.onClick()
                        }) {
                            Text(
                                text = stringResource(menuItem.textRes)
                            )
                        }
                    }
                    is MenuItem.Toggle -> {
                        val checked = menuItem.checked.safeValue

                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            menuItem.onCheckedChange(!checked)
                        }) {
                            Text(
                                text = menuItem.text(checked)
                            )
                        }
                    }
                }

                if (index < menuItems.size - 1) Divider()
            }
        }
    }
}

@Composable
private fun defaultColor(): Color =
    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)

@Composable
private fun MenuItem.Toggle.color(checked: Boolean): Color =
    if (checked) checkedColorRes?.let { colorResource(it) } ?: defaultColor()
    else uncheckedColorRes?.let { colorResource(it) } ?: defaultColor()

@Composable
private fun MenuItem.Toggle.text(checked: Boolean): String =
    if (checked) stringResource(checkedTextRes)
    else stringResource(uncheckedTextRes)

