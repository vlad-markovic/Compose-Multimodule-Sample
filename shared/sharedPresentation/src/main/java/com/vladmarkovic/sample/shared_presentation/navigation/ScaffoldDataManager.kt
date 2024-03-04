package com.vladmarkovic.sample.shared_presentation.navigation

import com.vladmarkovic.sample.shared_presentation.compose.DrawerData
import com.vladmarkovic.sample.shared_presentation.compose.ScreenChange
import com.vladmarkovic.sample.shared_presentation.compose.TopBarData
import com.vladmarkovic.sample.shared_presentation.compose.toData
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
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
    @Assisted initialScreen: Screen
) : ScaffoldDataUpdater, ScaffoldDataMonitor {

    private val _screen: MutableStateFlow<Screen> = MutableStateFlow(initialScreen)
    override val currentScreen: StateFlow<Screen> = _screen.asStateFlow()

    private val _holderType: MutableStateFlow<ScreenHolderType> = MutableStateFlow(ScreenHolderType.TAB)
    override val holderType: StateFlow<ScreenHolderType> = _holderType.asStateFlow()

    private val _topBar: MutableStateFlow<TopBarData?> = MutableStateFlow(null)
    override val topBar: StateFlow<TopBarData?> = _topBar.asStateFlow()

    private val _drawer: MutableStateFlow<DrawerData?> = MutableStateFlow(null)
    override val drawer: StateFlow<DrawerData?> = _drawer.asStateFlow()

    override fun update(change: ScreenChange) {
        _screen.update(change.screenChange.screen)
        _holderType.update(change.screenChange.holderType)
        _topBar.updateNullable(change.topBarChange.toData(_topBar.value))
        _drawer.updateNullable(change.drawerChange.toData(_drawer.value))
    }
}

@AssistedFactory
interface ScaffoldDataManagerFactory : BaseAssistedFactory<ScaffoldDataManager, Screen> {
    override fun create(assistedInput: Screen): ScaffoldDataManager
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ScaffoldDataManagerFactoryProvider {
    fun provideScaffoldDataManagerFactory(): ScaffoldDataManagerFactory
}
