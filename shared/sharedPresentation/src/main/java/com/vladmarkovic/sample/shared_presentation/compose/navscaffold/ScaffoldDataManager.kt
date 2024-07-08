package com.vladmarkovic.sample.shared_presentation.compose.navscaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.compose.di.activityFactoryProvider
import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
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


class ScaffoldDataManager @AssistedInject constructor(
    @Assisted initialScreen: Screen?
) {

    private val _screen: MutableStateFlow<Screen?> = MutableStateFlow(initialScreen)
    val currentScreen: StateFlow<Screen?> = _screen.asStateFlow()

    private val _topBarData: MutableStateFlow<TopBarData?> = MutableStateFlow(null)
    val topBarData: StateFlow<TopBarData?> = _topBarData.asStateFlow()

    private val _drawerData: MutableStateFlow<DrawerData?> = MutableStateFlow(null)
    val drawerData: StateFlow<DrawerData?> = _drawerData.asStateFlow()

    fun update(data: ScaffoldData) {
        _topBarData.updateNullable(data.topBar)
        _drawerData.updateNullable(data.drawerItems)
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

@Composable
fun rememberScaffoldDataManager(initialScreen: Screen?, key: String? = null): ScaffoldDataManager {
    val factory = activityFactoryProvider<ScaffoldDataManagerFactoryProvider>().provideScaffoldDataManagerFactory()
    return remember(key) { factory.create(initialScreen) }
}