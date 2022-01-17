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
import androidx.compose.ui.unit.dp

@Composable
fun DefaultDrawer(_drawerItems: State<List<DrawerItem>>, closeDrawer: () -> Unit) {
    val drawerItems by _drawerItems

    Spacer(Modifier.padding(8.dp))

    Column(
        Modifier
            .wrapContentWidth()
            .padding(16.dp)
    ) {
        drawerItems.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        closeDrawer()
                        item.onClick()
                    }
            ) {
                Icon(
                    modifier = Modifier.padding(end = 4.dp).align(Alignment.CenterVertically),
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
