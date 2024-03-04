/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@file:Suppress("FunctionName")

package com.vladmarkovic.sample.shared_presentation.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.shared_presentation.compose.DrawerData
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun defaultDrawer(
    scaffoldState: ScaffoldState,
    mainScope: CoroutineScope,
    drawerData: DrawerData?
): (@Composable ColumnScope.() -> Unit)? {
    if (drawerData == null || drawerData.items.isEmpty()) return null
    else return { DefaultDrawer(scaffoldState, mainScope, drawerData) }
}

@Composable
fun DefaultDrawer(scaffoldState: ScaffoldState, mainScope: CoroutineScope, drawerData: DrawerData) {
    DefaultDrawer(drawerData.items) {
        mainScope.launch { scaffoldState.drawerState.close() }
    }
}

@Composable
fun DefaultDrawer(drawerItems: List<DrawerItem>, closeDrawer: () -> Unit) {
    Spacer(Modifier.padding(Dimens.m / 2))

    Column(
        Modifier
            .wrapContentWidth()
            .padding(Dimens.m)
    ) {
        drawerItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        closeDrawer()
                        item.onClick()
                    }
                    .then(Modifier.padding(Dimens.m))

            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = Dimens.m / 4)
                        .align(Alignment.CenterVertically),
                    imageVector = item.icon,
                    contentDescription = null
                )
                Text(
                    text = stringResource(item.textRes),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}
