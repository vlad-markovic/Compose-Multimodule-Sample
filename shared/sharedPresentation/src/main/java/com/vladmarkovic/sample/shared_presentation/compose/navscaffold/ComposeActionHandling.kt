/** Copyright (C) 2024 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.vladmarkovic.sample.common.logging.Lumber
import com.vladmarkovic.sample.common.view.action.NavigationAction
import com.vladmarkovic.sample.common.view.action.ViewAction
import com.vladmarkovic.sample.shared_domain.tab.Tab
import com.vladmarkovic.sample.shared_presentation.compose.ComposeNavArgs
import com.vladmarkovic.sample.shared_presentation.compose.ComposeScreenContentResolver
import com.vladmarkovic.sample.shared_presentation.compose.onBack
import com.vladmarkovic.sample.shared_presentation.compose.openDrawer
import com.vladmarkovic.sample.shared_presentation.display.CommonDisplayAction
import com.vladmarkovic.sample.shared_presentation.navigation.CommonNavigationAction
import com.vladmarkovic.sample.shared_presentation.navigation.ToNavGraphScreen
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreenGroup
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.navigate
import com.vladmarkovic.sample.shared_presentation.screen.ToTab
import com.vladmarkovic.sample.shared_presentation.screen.routeWithArgs
import com.vladmarkovic.sample.shared_presentation.util.handleTopScreenNavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
internal class ComposeScreenContentResolverProvider @Inject constructor(
    val resolver: ComposeScreenContentResolver
) : ViewModel()

@Composable
fun injectScreenContentResolver(): ComposeScreenContentResolver =
    hiltViewModel<ComposeScreenContentResolverProvider>().resolver

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
fun <T> rememberTabNavHandler(
    tabNav: T,
    bubbleUp: (ViewAction) -> Unit,
    key: String? = null
)  : (ViewAction) -> Unit where T: ViewModel, T: MutableStateFlow<Tab> =
    remember(key) {{ action ->
        when (action) {
            is ToTab -> tabNav.navigate(action.tab)
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
        is CommonDisplayAction -> handleCommonDisplayAction(action)
        else -> bubbleUp(action)
    }
}

/** Branch out handling of different types of [ViewAction.NavigationAction]s. */
private fun ComposeNavArgs.navigate(action: NavigationAction, bubbleUp: (ViewAction) -> Unit) = when(action) {
    is ToNavGraphScreen -> navController.navigate(action.routeWithArgs)
    is ToScreenGroup -> navController.context.handleTopScreenNavigationAction(action)
    else -> bubbleUp(action)
}

private fun ComposeNavArgs.handleCommonDisplayAction(action: CommonDisplayAction) =
    when(action) {
        is CommonDisplayAction.Toast -> {
            Toast.makeText(navController.context, action.value.get(navController.context), Toast.LENGTH_SHORT).show()
        }
    }
