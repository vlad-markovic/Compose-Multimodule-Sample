/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.shared_domain.di.EntryPointAccessor
import com.vladmarkovic.sample.shared_domain.log.Lumber
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.compose.di.ScreenContentResolverEntryPoint
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreenGroup
import com.vladmarkovic.sample.shared_presentation.navigation.route
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.screen.ToTab
import com.vladmarkovic.sample.shared_presentation.util.handleTopScreenNavigationAction
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberScreenContentResolver(key: String? = null) = remember(key) {
    EntryPointAccessor.fromApplication(ScreenContentResolverEntryPoint::class.java).screenContentResolver()
}

@Composable
fun rememberScaffoldChangesHandler(
    scaffoldData: ScaffoldDataManager,
    bubbleUp: (BriefAction) -> Unit,
    key: String? = null
)  : (BriefAction) -> Unit =
    remember(key) {{ action ->
        when (action) {
            is ScaffoldData -> scaffoldData.update(action)
            else -> bubbleUp(action)
        }
    }}

@Composable
fun <T> rememberTabNavHandler(
    tabNav: T,
    bubbleUp: (BriefAction) -> Unit,
    key: String? = null
)  : (BriefAction) -> Unit where T: ViewModel, T: MutableStateFlow<Tab> =
    remember(key) {{ action ->
        when (action) {
            is ToTab -> tabNav.navigate(action.tab)
            else -> bubbleUp(action)
        }
    }}

@Composable
fun rememberThrowingNoHandler(key: String? = null): (BriefAction) -> Unit =
    remember(key) {{ throw IllegalStateException("Unhandled action: $it") }}

internal fun ComposeNavArgs.handleAction(action: BriefAction, bubbleUp: (BriefAction) -> Unit) {
    Lumber.i("action: ${action.javaClass.simpleName}")
    return when (action) {
        is BriefAction.NavigationAction -> navigate(action, bubbleUp)
        is CommonDisplayAction -> handleCommonDisplayAction(action)
        else -> bubbleUp(action)
    }
}

/** Branch out handling of different types of [NavigationAction]s. */
private fun ComposeNavArgs.navigate(action: BriefAction.NavigationAction, bubbleUp: (BriefAction) -> Unit) = when(action) {
    is ToScreen -> navController.navigate(action.route)
    is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
    else -> bubbleUp(action)
}

private fun ComposeNavArgs.handleCommonDisplayAction(action: CommonDisplayAction) =
    when(action) {
        is CommonDisplayAction.Toast -> {
            Toast.makeText(navController.context, action.value.get(navController.context), Toast.LENGTH_SHORT).show()
        }
    }