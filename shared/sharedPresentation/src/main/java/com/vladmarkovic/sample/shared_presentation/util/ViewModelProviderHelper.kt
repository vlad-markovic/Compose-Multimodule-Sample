/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.shared_presentation.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefAction
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActioner
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import kotlin.reflect.KClass

// region Activity
/**
 * After initialization, the [BriefActionViewModel] is setup with observing common [BriefAction]s.
 *
 * Returns a [Lazy] delegate to access the ComponentActivity's [BriefActionViewModel];
 * if [factoryProducer] is specified then [ViewModelProvider.Factory] returned by it will be used
 * to create [BriefActionViewModel] first time.
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     val viewModel: MyViewModel by actionViewModels()
 * }
 * ```
 *
 * Custom [ViewModelProvider.Factory] can be defined via [factoryProducer] parameter,
 * factory returned by it will be used to create [BriefActionViewModel]:
 * ```
 * class MyComponentActivity : FragmentActivity() {
 *     val viewModel: MyViewModel by actionViewModels({ myFactory })
 * }
 * ```
 *
 * This property can be accessed only after the Activity is attached to the Application,
 * and access prior to that will result in IllegalArgumentException.
 */
@MainThread
inline fun <reified VM : BriefActionViewModel> FragmentActivity.actionViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = ViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { viewModelStore },
    factoryProducer = factoryProducer ?: { defaultViewModelProviderFactory },
    setup = { viewModel ->
        viewModel.action.observeNonNull(this) { action ->
            handleBriefAction(action)
        }
    }
)
// endregion Activity

// region Fragment
/**
 * After initialization, the [BriefActionViewModel] is setup with observing common [BriefAction]s.
 *
 * Returns a property delegate to access [BriefActionViewModel] by default scoped to this [Fragment]
 * ```
 * class MyFragment : Fragment() {
 *     val viewModel: MyViewModel by actionViewModels()
 * }
 * ```
 *
 * Custom [ViewModelProvider.Factory] can be defined via [factoryProducer] parameter,
 * factory returned by it will be used to create [ViewModel]:
 * ```
 * class MyFragment : Fragment() {
 *     val viewModel: MyViewModel by actionViewModels({ myFactory })
 * }
 * ```
 *
 * Default scope may be overridden with parameter [ownerProducer]:
 * ```
 * class MyFragment : Fragment() {
 *     val viewModel: MyViewModel by actionViewModels({requireParentFragment()})
 * }
 * ```
 *
 * This property can be accessed only after this Fragment is attached i.e., after
 * [Fragment.onAttach()], and access prior to that will result in IllegalArgumentException.
 */
@MainThread
inline fun <reified VM : BriefActionViewModel> Fragment.actionViewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = createViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { ownerProducer().viewModelStore },
    factoryProducer = factoryProducer,
    setup = { viewModel ->
        viewModel.action.observeNonNull(requireActivity()) { action ->
            handleBriefAction(action)
        }
    }
)

/**
 * After initialization, the [BriefActionViewModel] is setup with observing common [BriefAction]s.
 *
 * Returns a property delegate to access parent activity's [BriefActionViewModel],
 * if [factoryProducer] is specified then [ViewModelProvider.Factory]
 * returned by it will be used to create [BriefActionViewModel] first time. Otherwise, the activity's
 * [androidx.activity.ComponentActivity.getDefaultViewModelProviderFactory](default factory)
 * will be used.
 *
 * ```
 * class MyFragment : Fragment() {
 *     val viewModel: MyViewModel by activityActionViewModels()
 * }
 * ```
 *
 * This property can be accessed only after this Fragment is attached i.e., after
 * [Fragment.onAttach()], and access prior to that will result in IllegalArgumentException.
 */
@MainThread
inline fun <reified VM : BriefActionViewModel> Fragment.activityActionViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = createViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { requireActivity().viewModelStore },
    factoryProducer = factoryProducer ?: { requireActivity().defaultViewModelProviderFactory },
    setup = { viewModel ->
        viewModel.action.observeNonNull(viewLifecycleOwner) { action ->
            handleBriefAction(action)
        }
    }
)

/** Helper method for creation of [ViewModelLazy] */
@MainThread
fun <VM : ViewModel> Fragment.createViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    setup: (VM) -> Unit
): Lazy<VM> = ViewModelLazy(
    viewModelClass = viewModelClass,
    storeProducer = storeProducer,
    factoryProducer = factoryProducer ?: { defaultViewModelProviderFactory },
    setup = setup
)

/**
 * An implementation of [Lazy] used by [FragmentActivity.actionViewModels],
 * [Fragment.actionViewModels] and [Fragment.activityActionViewModels].
 *
 * [storeProducer] is a lambda that will be called during initialization, [VM] will be created
 * in the scope of returned [ViewModelStore].
 *
 * [factoryProducer] is a lambda that will be called during initialization,
 * returned [ViewModelProvider.Factory] will be used for creation of [VM]
 *
 * [setup] is a lambda that will be called after initialization, passing the [ViewModel]
 * to be able to apply some setup on in, like setting up observing actions.
 */
class ViewModelLazy<VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val setup: (VM) -> Unit
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory)[viewModelClass.java].also {
                    setup(it)
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized(): Boolean = cached != null
}
// endregion Fragment

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
inline fun <reified VM : BriefActionViewModel> actionViewModel(navController: NavHostController): VM =
    androidx.hilt.navigation.compose.hiltViewModel<VM>().apply { actioner.setupWith(navController) }

@Composable
inline fun <reified VM : BriefActionViewModel> actionViewModel(
    navController: NavHostController,
    factory: ViewModelProvider.Factory
): VM = hiltViewModel<VM>(navController.currentBackStackEntry!!, factory)
    .apply { actioner.setupWith(navController) }

/** Setup observing of [BriefAction]s for a [BriefActionViewModel]. */
fun BriefActioner.setupWith(navController: NavHostController) {
    action.observeNonNull(navController.context as ComponentActivity) { action ->
        navController.handleBriefAction(action)
    }
}
// endregion Compose
