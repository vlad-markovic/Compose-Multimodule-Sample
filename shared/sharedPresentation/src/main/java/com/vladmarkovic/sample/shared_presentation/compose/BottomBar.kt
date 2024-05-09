package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.tab.icon
import com.vladmarkovic.sample.shared_presentation.tab.textRes
import kotlinx.coroutines.flow.StateFlow


@Composable
fun DefaultBottomBar(
    tabs: List<Tab>,
    currentTabFlow: StateFlow<Tab>,
    onTabSelected: (Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Don't show tabs if there is none or only one.
    if (tabs.size < 2) return

    val currentTab by currentTabFlow.collectAsState()

    BottomAppBar(modifier) {
        BottomNavigation {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(tab.icon, contentDescription = null) },
                    label = { Text(stringResource(tab.textRes)) },
                    alwaysShowLabel = false,
                    selected = currentTab == tab,
                    onClick = { onTabSelected(tab) }
                )
            }
        }
    }
}
