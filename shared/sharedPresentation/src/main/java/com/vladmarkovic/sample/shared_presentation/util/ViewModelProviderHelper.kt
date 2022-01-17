/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActioner
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.TabNavigableComposeHolder
import com.vladmarkovic.sample.shared_presentation.navigation.tabbed.tabNavViewModelProviderFactory
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

@MainThread
fun <S: Screen, T: Tab<S>> TabNavigableComposeHolder<S, T>.tabNavViewModels(): Lazy<TabNavViewModel<S, T>> =
    with(this as FragmentActivity) {
        viewModels { tabNavViewModelProviderFactory(tabNavViewModelFactory, mainTab) }
    }

// region Compose
fun hiltViewModelFactory(
    context: Context,
    navBackStackEntry: NavBackStackEntry,
    factory: ViewModelProvider.Factory = navBackStackEntry.defaultViewModelProviderFactory
): ViewModelProvider.Factory = HiltViewModelFactory.createInternal(
    context.asActivity,
    navBackStackEntry,
    navBackStackEntry.arguments,
    factory,
)

@Composable
inline fun <reified VM : ViewModel> hiltViewModel(
    navBackStackEntry: NavBackStackEntry,
    factory: ViewModelProvider.Factory = navBackStackEntry.defaultViewModelProviderFactory
): VM = viewModel(
    viewModelStoreOwner = navBackStackEntry,
    factory = hiltViewModelFactory(LocalContext.current, navBackStackEntry, factory)
)

@Composable
inline fun <reified VM : BriefActionViewModel> actionViewModel(contentArgs: ContentArgs): VM =
    androidx.hilt.navigation.compose.hiltViewModel<VM>().apply { actioner.setupWith(contentArgs) }

@Composable
inline fun <reified VM : BriefActionViewModel> actionViewModel(
    contentArgs: ContentArgs,
    factory: ViewModelProvider.Factory
): VM = hiltViewModel<VM>(contentArgs.backStackEntry, factory)
    .apply { actioner.setupWith(contentArgs) }

/** Setup observing of [BriefAction]s for a [BriefActionViewModel]. */
fun BriefActioner.setupWith(contentArgs: ContentArgs) {
    action.observeNonNull(contentArgs.context as ComponentActivity) { action ->
        contentArgs.handleBriefAction(action)
    }
}
// endregion Compose
