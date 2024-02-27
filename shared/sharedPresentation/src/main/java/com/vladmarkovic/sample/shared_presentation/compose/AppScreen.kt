/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
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
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppColor
import com.vladmarkovic.sample.shared_presentation.ui.theme.AppTheme
import com.vladmarkovic.sample.shared_presentation.util.safeValue

@Composable
fun DefaultTopBar(
    title: StrOrRes,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    up: UpButton? = null,
    menuItems: Array<MenuItem>? = null,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = AppColor.Grey900,
        navigationIcon = { up?.let { Up(it) } },
        actions = { menuItems?.let { DefaultMenu(it) } },
        elevation = elevation,
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
fun Up(upButton: UpButton?) {
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
    DefaultTopBar(StrOrRes.str("Top Title"))
}

@Composable
fun DefaultMenu(menuItems: Array<MenuItem>) {
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

