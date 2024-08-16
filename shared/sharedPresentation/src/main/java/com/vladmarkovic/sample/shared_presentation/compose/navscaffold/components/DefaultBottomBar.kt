package com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.common.navigation.screen.model.Screen
import com.vladmarkovic.sample.common.navigation.tab.model.Tab
import kotlinx.coroutines.flow.StateFlow


@Composable
fun <S : Screen, T : Tab<S>> DefaultBottomBar(
    allTabs: List<T>,
    currentTabFlow: StateFlow<Tab<*>>,
    onTabSelected: (T) -> Unit,
    resolver: (T) -> Pair<ImageVector, Int>,
    modifier: Modifier = Modifier,
) {
    // Don't show tabs if there is none or only one.
    if (allTabs.size < 2) return

    val currentTab by currentTabFlow.collectAsState()

    BottomAppBar(modifier) {
        BottomNavigation {
            allTabs.forEach { tab ->
                val (icon, textRes) = resolver(tab)
                BottomNavigationItem(
                    icon = { Icon(icon, contentDescription = null) },
                    label = { Text(stringResource(textRes)) },
                    alwaysShowLabel = false,
                    selected = currentTab == tab,
                    onClick = { onTabSelected(tab) }
                )
            }
        }
    }
}
