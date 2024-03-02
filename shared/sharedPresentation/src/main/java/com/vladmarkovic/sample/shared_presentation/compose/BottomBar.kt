package com.vladmarkovic.sample.shared_presentation.compose

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigable
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.safeValue


@Composable
fun <S: Screen, T : Tab<S>> DefaultBottomBar(tabNav: TabNavigable<S, T>, tabs: List<T>) {
    // Don't show tabs if there is only one.
    if (tabs.size > 1) {
        DefaultBottomBar(
            tabs = tabs,
            currentTab = tabNav.tab.safeValue,
            navToTab = { tabNav.navigate(it) }
        )
    }
}

@Composable
fun <S, T : Tab<S>> DefaultBottomBar(
    tabs: List<T>,
    currentTab: T,
    navToTab: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier) {
        BottomNavigation {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(tab.icon, contentDescription = null) },
                    label = { Text(stringResource(tab.textRes)) },
                    alwaysShowLabel = false,
                    selected = currentTab == tab,
                    onClick = { navToTab(tab) }
                )
            }
        }
    }
}
