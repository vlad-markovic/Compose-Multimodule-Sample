/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActioner
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.tabNavViewModelProviderFactory
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

@MainThread
fun <S: Screen, T: Tab<S>> TabNavigableComposeHolder<S, T>.tabNavViewModels(): Lazy<TabNavViewModel<S, T>> =
    with(this as FragmentActivity) {
        viewModels { tabNavViewModelProviderFactory(tabNavViewModelFactory, mainTab) }
    }

// region Compose
//fun hiltViewModelFactory(
//    context: Context,
//    navBackStackEntry: NavBackStackEntry,
//    factory: ViewModelProvider.Factory = navBackStackEntry.defaultViewModelProviderFactory
//): ViewModelProvider.Factory = HiltViewModelFactory.createInternal(
//    context.asActivity,
//    navBackStackEntry,
//    navBackStackEntry.arguments,
//    factory,
//)
//
//@Composable
//inline fun <reified VM : ViewModel> hiltViewModel(
//    navBackStackEntry: NavBackStackEntry,
//    factory: ViewModelProvider.Factory = navBackStackEntry.defaultViewModelProviderFactory,
//    key: String? = null
//): VM = viewModel(
//    viewModelStoreOwner = navBackStackEntry,
//    factory = hiltViewModelFactory(LocalContext.current, navBackStackEntry, factory),
//    key = key
//)
//
//@Composable
//inline fun <reified VM> actionViewModel(
//    backStackEntry: NavBackStackEntry,
//    noinline actionHandler: (BriefAction) -> Unit,
//    factory: ViewModelProvider.Factory = backStackEntry.defaultViewModelProviderFactory,
//    key: String? = null
//): VM where VM : BriefActionable, VM : ViewModel =
//    hiltViewModel<VM>(backStackEntry, factory, key).apply { actioner.SetupWith(actionHandler) }

@Composable
inline fun <reified VM> actionViewModel(
    noinline actionHandler: (BriefAction) -> Unit,
): VM where VM : BriefActionable, VM : ViewModel =
    actionViewModel(key = null, actionHandler)


@Composable
inline fun <reified VM> actionViewModel(
    key: String?,
    noinline actionHandler: (BriefAction) -> Unit
): VM where VM : BriefActionable, VM : ViewModel =
    androidx.hilt.navigation.compose.hiltViewModel<VM>(key = key).apply { actioner.SetupWith(actionHandler) }

/** Setup observing of [BriefAction]s for a [BriefActionViewModel]. */
@Composable
fun BriefActioner.SetupWith(actionHandler: (BriefAction) -> Unit) {
    ActionsHandler(action, actionHandler)
}

@Composable
fun <T: BriefAction> ActionsHandler(actions: Flow<T>, handler: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareActions = remember(actions, lifecycleOwner) {
        actions.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    LaunchedEffect(actions) {
        lifecycleAwareActions
            .onEach(handler)
            .flowOn(EmptyCoroutineContext)
            .launchIn(this)
    }
}
// endregion Compose
