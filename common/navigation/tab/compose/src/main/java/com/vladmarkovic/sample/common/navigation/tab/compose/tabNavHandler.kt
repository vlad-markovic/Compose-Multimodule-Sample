package com.vladmarkovic.sample.common.navigation.tab.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.TabNavViewModel
import com.vladmarkovic.sample.common.navigation.tab.navcomponent.model.ToTab
import com.vladmarkovic.sample.common.mv.action.Action

@Composable
fun rememberTabNavHandler(
    tabNav: TabNavViewModel,
    bubbleUp: (Action) -> Unit,
    key: String? = null
)  : (Action) -> Unit =
    remember(key) {
        { action ->
            when (action) {
                is ToTab -> tabNav.navigate(action.tab)
                else -> bubbleUp(action)
            }
        }
    }