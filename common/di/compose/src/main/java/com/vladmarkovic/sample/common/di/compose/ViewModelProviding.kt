/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.common.di.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import com.vladmarkovic.sample.common.di.viewmodel.AssistedViewModelFactory
import com.vladmarkovic.sample.core.android.asActivity
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.withCreationCallback


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

// region assisted ViewModel
@Composable
inline fun <reified VM : ViewModel, I, VMF : AssistedViewModelFactory<VM, I>> assistedViewModel(
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
inline fun <reified VM : ViewModel, I, VMF : AssistedViewModelFactory<VM, I>> assistedViewModel(
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
inline fun <reified VM : ViewModel> sharedViewModel(
    activity: ComponentActivity = LocalContext.current.asActivity<ComponentActivity>(),
    viewModelStoreOwner: ViewModelStoreOwner = activity,
    factory: ViewModelProvider.Factory? = activity.defaultViewModelProviderFactory,
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory)

@Composable
inline fun <reified VM : ViewModel, I, VMF : AssistedViewModelFactory<VM, I>> sharedAssistedViewModel(
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
inline fun <reified VM : ViewModel, I, VMF : AssistedViewModelFactory<VM, I>> sharedAssistedViewModel(
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

@Composable
inline fun <reified VM : ViewModel> stackViewModel(
    navBackStackEntry: NavBackStackEntry? = rememberNavController().currentBackStackEntry,
    viewModelStoreOwner: ViewModelStoreOwner = navBackStackEntry ?: checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    factory: ViewModelProvider.Factory? = navBackStackEntry?.let {
        HiltViewModelFactory(
            LocalContext.current,
            navBackStackEntry
        )
    },
    key: String? = null,
): VM = viewModel(viewModelStoreOwner, key, factory)


@Composable
inline fun <reified P> activityFactoryProvider(): P {
    return EntryPointAccessors.fromActivity(LocalContext.current.asActivity, P::class.java)
}
