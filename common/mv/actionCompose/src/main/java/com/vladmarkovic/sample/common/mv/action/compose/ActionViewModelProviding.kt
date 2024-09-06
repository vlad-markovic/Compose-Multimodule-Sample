/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.mv.action.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.common.di.viewmodel.AssistedViewModelFactory
import com.vladmarkovic.sample.common.mv.action.ActionViewModel
import com.vladmarkovic.sample.common.mv.action.Action
import com.vladmarkovic.sample.common.mv.action.ActionMonitorable
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext


@Composable
inline fun <reified VM> actionViewModel(
    key: String?,
    noinline actionHandler: (Action) -> Unit,
): VM where VM : ViewModel, VM : ActionMonitorable<Action> =
    hiltViewModel<VM>(key = key).apply { SetupWith(actionHandler) }

@Composable
inline fun <reified VM> actionViewModel(
    noinline actionHandler: (Action) -> Unit,
): VM where VM : ViewModel, VM : ActionMonitorable<Action> =
    hiltViewModel<VM>().apply { SetupWith(actionHandler) }

@Composable
inline fun <reified VM, I, VMF : AssistedViewModelFactory<VM, I>> assistedActionViewModel(
    assistedInput: I,
    noinline actionHandler: (Action) -> Unit,
    navBackStackEntry: NavBackStackEntry? = rememberNavController().currentBackStackEntry,
    viewModelStoreOwner: ViewModelStoreOwner = navBackStackEntry ?: checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    factory: ViewModelProvider.Factory? = navBackStackEntry?.let { HiltViewModelFactory(LocalContext.current, it) },
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }.withCreationCallback<VMF> { assistedFactory ->
        assistedFactory.create(assistedInput)
    },
    key: String? = null,
): VM where VM : ViewModel, VM : ActionMonitorable<Action> =
    viewModel<VM>(viewModelStoreOwner, key, factory, extras).apply { SetupWith(actionHandler) }

// region setup action handling
/** Setup observing of [Action]s for a [ActionViewModel]. */
@Composable
fun ActionMonitorable<Action>.SetupWith(actionHandler: (Action) -> Unit) {
    ActionsHandler(this.actions, actionHandler)
}


@Composable
fun <T : Action> ActionsHandler(actions: Flow<T>, handler: (T) -> Unit) {
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
// endregion setup action handling
