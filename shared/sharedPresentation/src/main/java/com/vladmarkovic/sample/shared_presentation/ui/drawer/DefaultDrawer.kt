/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@file:Suppress("FunctionName")

package com.vladmarkovic.sample.shared_presentation.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens

@Composable
fun DefaultDrawer(_drawerItems: State<List<DrawerItem>>, closeDrawer: () -> Unit) {
    val drawerItems by _drawerItems

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
