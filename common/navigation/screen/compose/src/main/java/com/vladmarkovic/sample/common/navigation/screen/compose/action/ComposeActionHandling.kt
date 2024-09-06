/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.compose.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.mv.action.NavigationAction
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.navigation.screen.compose.model.ComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.compose.util.onBack
import com.vladmarkovic.sample.common.navigation.screen.compose.util.openDrawer
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToNavGraphScreen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.routeWithArgs
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberCommonActionsHandler(
    navArgs: ComposeNavArgs,
    scope: CoroutineScope,
    bubbleUp: (Action) -> Unit,
    key: String? = null
)  : (Action) -> Unit = remember(key) {{ action ->
    when(action) {
        is CommonNavigationAction.Back -> navArgs.onBack(scope)
        is CommonNavigationAction.OpenDrawer -> navArgs.openDrawer(scope)
        else -> navArgs.handleAction(action, bubbleUp)
    }
}}

@Composable
fun rememberThrowingNoHandler(key: String? = null): (Action) -> Unit =
    remember(key) {{ throw IllegalStateException("Unhandled action: $it") }}

internal fun ComposeNavArgs.handleAction(action: Action, bubbleUp: (Action) -> Unit) {
    Lumber.i("action: ${action.javaClass.simpleName}")
    return when (action) {
        is NavigationAction -> navigate(action, bubbleUp)
        else -> bubbleUp(action)
    }
}

/** Branch out handling of different types of [Action.NavigationAction]s. */
private fun ComposeNavArgs.navigate(action: NavigationAction, bubbleUp: (Action) -> Unit) = when(action) {
    is ToNavGraphScreen -> navController.navigate(action.routeWithArgs)
    else -> bubbleUp(action)
}
