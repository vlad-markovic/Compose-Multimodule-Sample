/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import androidx.activity.ComponentActivity
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
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionable
import com.vladmarkovic.sample.shared_presentation.di.AssistedViewModelFactory
import com.vladmarkovic.sample.shared_presentation.navigation.ScaffoldDataManager
import com.vladmarkovic.sample.shared_presentation.navigation.ScaffoldDataManagerFactoryProvider
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModelFactory
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigator
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigatorFactoryProvider
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

//@MainThread
//fun <S: Screen, T: Tab<S>> TabNavigableComposeHolder<S, T>.tabNavViewModels(): Lazy<TabNavViewModel<S, T>> =
//    with(this as FragmentActivity) {
//        viewModels { tabNavViewModelProviderFactory(tabNavViewModelFactory, mainTab) }
//    }

@Composable
inline fun <reified VM> actionViewModel(
    key: String?,
    noinline actionHandler: (BriefAction) -> Unit,
): VM where VM : BriefActionable, VM : ViewModel =
    hiltViewModel<VM>(key = key).apply { SetupWith(actionHandler) }


@Composable
inline fun <reified VM> actionViewModel(
    noinline actionHandler: (BriefAction) -> Unit,
): VM where VM : BriefActionable, VM : ViewModel =
    hiltViewModel<VM>().apply { SetupWith(actionHandler) }

@Composable
inline fun <reified VM : ViewModel> stackHiltViewModel(
    navBackStackEntry: NavBackStackEntry? = rememberNavController().currentBackStackEntry,
    viewModelStoreOwner: ViewModelStoreOwner = navBackStackEntry ?: checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    factory: ViewModelProvider.Factory? = navBackStackEntry?.let { HiltViewModelFactory(LocalContext.current, navBackStackEntry) },
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory)


//@Composable
//inline fun <reified VM: ViewModel, I, reified F, reified P> assistedViewModel(
//    assistedInput: I,
//    key: String? = null
//): VM where F : AssistedViewModelFactory<VM, I>, P : BaseFactoryProvider<F> =
//    viewModel(
//        factory = viewModelProviderFactory(
//            assistedFactory = activityAssistedFactory<F, P>(),
//            assistedInput = assistedInput
//        ),
//        key = key
//    )
//

@Composable
inline fun <reified P> activityFactoryProvider(): P {
    return EntryPointAccessors.fromActivity(LocalContext.current.asActivity, P::class.java)
}

// region assisted ViewModel
@Composable
inline fun <reified VM : ViewModel, I, VMF: AssistedViewModelFactory<VM, I>> assistedViewModel(
    crossinline inputProvider: () -> I,
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
        assistedFactory.create(inputProvider())
    },
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory, extras)

@Composable
inline fun <reified VM : ViewModel, I, VMF: AssistedViewModelFactory<VM, I>> assistedViewModel(
    assistedInput: I,
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
): VM = viewModel(viewModelStoreOwner, key, factory, extras)
// endregion assisted ViewModel


// region shared ViewModel
@Composable
fun tabNavViewModel(tabNav: TabNavigator, key: String? = null): TabNavViewModel =
    assistedViewModel<TabNavViewModel, TabNavigator, TabNavViewModelFactory>(assistedInput = tabNav, key = key)

@Composable
fun tabNavigator(initialTab: Tab, key: String? = null): TabNavigator {
    val factory = activityFactoryProvider<TabNavigatorFactoryProvider>().provideTabNavigatorFactory()
    return remember(key) { factory.create(initialTab) }
}


@Composable
fun scaffoldDataManager(initialScreen: Screen?, key: String? = null): ScaffoldDataManager {
    val factory = activityFactoryProvider<ScaffoldDataManagerFactoryProvider>().provideScaffoldDataManagerFactory()
    return remember(key) { factory.create(initialScreen) }
}

@Composable
inline fun <reified VM : ViewModel> sharedHiltViewModel(
    activity: ComponentActivity = LocalContext.current.asActivity<ComponentActivity>(),
    viewModelStoreOwner: ViewModelStoreOwner = activity,
    factory: ViewModelProvider.Factory? = activity.defaultViewModelProviderFactory,
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory)

@Composable
inline fun <reified VM : ViewModel, I, VMF: AssistedViewModelFactory<VM, I>> sharedAssistedViewModel(
    assistedInput: I,
    activity: ComponentActivity = LocalContext.current.asActivity<ComponentActivity>(),
    viewModelStoreOwner: ViewModelStoreOwner = activity,
    factory: ViewModelProvider.Factory? = activity.defaultViewModelProviderFactory,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }.withCreationCallback<VMF> { assistedFactory ->
        assistedFactory.create(assistedInput)
    },
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory, extras)

@Composable
inline fun <reified VM : ViewModel, I, VMF: AssistedViewModelFactory<VM, I>> sharedAssistedViewModel(
    crossinline inputProvider: () -> I,
    activity: ComponentActivity = LocalContext.current.asActivity<ComponentActivity>(),
    viewModelStoreOwner: ViewModelStoreOwner = activity,
    factory: ViewModelProvider.Factory? = activity.defaultViewModelProviderFactory,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }.withCreationCallback<VMF> { assistedFactory ->
        assistedFactory.create(inputProvider())
    },
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory, extras)
// region shared ViewModel


// region setup action handling
/** Setup observing of [BriefAction]s for a [BriefActionViewModel]. */
@Composable
fun BriefActionable.SetupWith(actionHandler: (BriefAction) -> Unit) {
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
// endregion setup action handling
