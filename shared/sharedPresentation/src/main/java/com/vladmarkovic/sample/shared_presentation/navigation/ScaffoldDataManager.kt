package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
import com.vladmarkovic.sample.shared_presentation.screen.Screen
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
//    @Assisted dataManager: ScaffoldDataManager
//) : ViewModel(), ScaffoldDataUpdater by dataManager, ScaffoldDataMonitor by dataManager
//
//@AssistedFactory
//interface ScaffoldViewModelFactory : AssistedViewModelFactory<ScaffoldViewModel, ScaffoldDataManager> {
//    override fun create(assistedInput: ScaffoldDataManager): ScaffoldViewModel
//}

class ScaffoldDataManager @AssistedInject constructor(
    @Assisted initialScreen: Screen?
) : ScaffoldDataMonitor, ScaffoldDataUpdater {

    private val _screen: MutableStateFlow<Screen?> = MutableStateFlow(initialScreen)
    override val currentScreen: StateFlow<Screen?> = _screen.asStateFlow()

    private val _topBar: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    override val topBar: StateFlow<(@Composable () -> Unit)?> = _topBar.asStateFlow()

    private val _drawer: MutableStateFlow<(@Composable ColumnScope.() -> Unit)?> = MutableStateFlow(null)
    override val drawer: StateFlow<(@Composable ColumnScope.() -> Unit)?> = _drawer.asStateFlow()

    override fun update(change: ScaffoldChange) {
        _screen.update(change.screen)
        _topBar.updateNullable(change.topBar)
        _drawer.updateNullable(change.drawer)
    }
}

@AssistedFactory
interface ScaffoldDataManagerFactory : BaseAssistedFactory<ScaffoldDataManager, Screen?> {
    override fun create(assistedInput: Screen?): ScaffoldDataManager
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ScaffoldDataManagerFactoryProvider {
    fun provideScaffoldDataManagerFactory(): ScaffoldDataManagerFactory
}
