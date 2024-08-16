/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.navigation.screen.compose.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.navigation.screen.compose.model.ComposeNavArgs
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.ScaffoldDataManager
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.ScaffoldData
import com.vladmarkovic.sample.common.navigation.screen.compose.util.onBack
import com.vladmarkovic.sample.common.navigation.screen.compose.util.openDrawer
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.CommonNavigationAction
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToNavGraphScreen
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.util.routeWithArgs
import com.vladmarkovic.sample.common.mv.action.NavigationAction
import com.vladmarkovic.sample.common.mv.action.ViewAction
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberScaffoldChangesHandler(
    scaffoldData: ScaffoldDataManager,
    bubbleUp: (ViewAction) -> Unit,
    key: String? = null
)  : (ViewAction) -> Unit =
    remember(key) {{ action ->
        when (action) {
            is ScaffoldData -> scaffoldData.update(action)
            else -> bubbleUp(action)
        }
    }}

@Composable
fun rememberCommonActionsHandler(
    navArgs: ComposeNavArgs,
    scope: CoroutineScope,
    bubbleUp: (ViewAction) -> Unit,
    key: String? = null
)  : (ViewAction) -> Unit = remember(key) {{ action ->
    when(action) {
        is CommonNavigationAction.Back -> navArgs.onBack(scope)
        is CommonNavigationAction.OpenDrawer -> navArgs.openDrawer(scope)
        else -> navArgs.handleAction(action, bubbleUp)
    }
}}

@Composable
fun rememberThrowingNoHandler(key: String? = null): (ViewAction) -> Unit =
    remember(key) {{ throw IllegalStateException("Unhandled action: $it") }}

internal fun ComposeNavArgs.handleAction(action: ViewAction, bubbleUp: (ViewAction) -> Unit) {
    Lumber.i("action: ${action.javaClass.simpleName}")
    return when (action) {
        is NavigationAction -> navigate(action, bubbleUp)
        else -> bubbleUp(action)
    }
}

/** Branch out handling of different types of [ViewAction.NavigationAction]s. */
private fun ComposeNavArgs.navigate(action: NavigationAction, bubbleUp: (ViewAction) -> Unit) = when(action) {
    is ToNavGraphScreen -> navController.navigate(action.routeWithArgs)
    else -> bubbleUp(action)
}
