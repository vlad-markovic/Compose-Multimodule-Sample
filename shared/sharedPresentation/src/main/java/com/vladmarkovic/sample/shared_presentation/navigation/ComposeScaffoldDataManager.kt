package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.update
import com.vladmarkovic.sample.shared_presentation.util.updateNullable
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//@HiltViewModel // TODO ScaffoldViewModel is it needed?
//class ScaffoldViewModel @AssistedInject constructor(
//    @Assisted dataManager: ComposeScaffoldDataManager
//) : ViewModel(), ScaffoldDataUpdater by dataManager, ScaffoldDataMonitor by dataManager
//
//@AssistedFactory
//interface ScaffoldViewModelFactory : AssistedViewModelFactory<ScaffoldViewModel, ComposeScaffoldDataManager> {
//    override fun create(assistedInput: ComposeScaffoldDataManager): ScaffoldViewModel
//}

class ComposeScaffoldDataManager @AssistedInject constructor(
    @Assisted initialScreen: Screen?
) {

    private val _screen: MutableStateFlow<Screen?> = MutableStateFlow(initialScreen)
    val currentScreen: StateFlow<Screen?> = _screen.asStateFlow()

    private val _topBar: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val topBar: StateFlow<(@Composable () -> Unit)?> = _topBar.asStateFlow()

    private val _drawer: MutableStateFlow<(@Composable ColumnScope.() -> Unit)?> = MutableStateFlow(null)
    val drawer: StateFlow<(@Composable ColumnScope.() -> Unit)?> = _drawer.asStateFlow()

    fun update(change: ScaffoldChange) {
        when (change) {
            is ScaffoldChange.ScreenChange -> {
                _screen.update(change.screen)
            }
            is ScaffoldChange.TopBarChange -> {
                _topBar.updateNullable(change.topBar)
            }
            is ScaffoldChange.DrawerChange -> {
                _drawer.updateNullable(change.drawer)
            }
        }
    }
}

@AssistedFactory
interface ScaffoldDataManagerFactory : BaseAssistedFactory<ComposeScaffoldDataManager, Screen?> {
    override fun create(assistedInput: Screen?): ComposeScaffoldDataManager
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ScaffoldDataManagerFactoryProvider {
    fun provideScaffoldDataManagerFactory(): ScaffoldDataManagerFactory
}
