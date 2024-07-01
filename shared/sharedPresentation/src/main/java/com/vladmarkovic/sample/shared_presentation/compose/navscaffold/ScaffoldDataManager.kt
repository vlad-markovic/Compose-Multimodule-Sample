package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DrawerItem
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.TopBarData
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
) {

    private val _screen: MutableStateFlow<Screen?> = MutableStateFlow(initialScreen)
    val currentScreen: StateFlow<Screen?> = _screen.asStateFlow()

    private val _topBarData: MutableStateFlow<TopBarData?> = MutableStateFlow(null)
    val topBarData: StateFlow<TopBarData?> = _topBarData.asStateFlow()

    private val _drawerItems: MutableStateFlow<List<DrawerItem>?> = MutableStateFlow(null)
    val drawerItems: StateFlow<List<DrawerItem>?> = _drawerItems.asStateFlow()

    fun update(data: ScaffoldData) {
        _topBarData.updateNullable(data.topBar)
        _drawerItems.updateNullable(data.drawerItems)
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
