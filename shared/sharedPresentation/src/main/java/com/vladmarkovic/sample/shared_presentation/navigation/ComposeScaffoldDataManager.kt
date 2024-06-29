package com.vladmarkovic.sample.shared_presentation.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.di.BaseAssistedFactory
import com.vladmarkovic.sample.shared_domain.screen.Screen
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.drawer.DrawerItem
import com.vladmarkovic.sample.shared_presentation.ui.model.MenuItem
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton
import com.vladmarkovic.sample.shared_presentation.util.safeValue
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

    private val _topBarTitle: MutableStateFlow<StrOrRes?> = MutableStateFlow(null)
    private val _topBarModifier: MutableStateFlow<Modifier> = MutableStateFlow(Modifier)
    private val _topBarTextAlign: MutableStateFlow<TextAlign> = MutableStateFlow(TextAlign.Start)
    private val _topBarUpButton: MutableStateFlow<UpButton?> = MutableStateFlow(null)
    private val _topBarMenuItems: MutableStateFlow<List<MenuItem>?> = MutableStateFlow(null)
    private val _topBarElevation: MutableStateFlow<Dp?> = MutableStateFlow(null)
    private val _topBarBackground: MutableStateFlow<Color?> = MutableStateFlow(null)

    @Composable
    fun topBar(): @Composable () -> Unit = _topBar.safeValue?.let {{
        it(
            _topBarTitle.asStateFlow(),
            _topBarModifier.asStateFlow(),
            _topBarTextAlign.asStateFlow(),
            _topBarUpButton.asStateFlow(),
            _topBarMenuItems.asStateFlow(),
            _topBarElevation.asStateFlow(),
            _topBarBackground.asStateFlow(),
        )
    }} ?: {}

    private val _topBar: MutableStateFlow<(@Composable (
        title: StateFlow<StrOrRes?>,
        modifier: StateFlow<Modifier>,
        textAlign: StateFlow<TextAlign>,
        upButton: StateFlow<UpButton?>,
        menuItems: StateFlow<List<MenuItem>?>,
        elevation: StateFlow<Dp?>,
        background: StateFlow<Color?>
    ) -> Unit)?> = MutableStateFlow(null)


    private val _drawerItems: MutableStateFlow<List<DrawerItem>?> = MutableStateFlow(null)
    private val _drawerModifier: MutableStateFlow<Modifier> = MutableStateFlow(Modifier)
    private val _drawerBackground: MutableStateFlow<Color?> = MutableStateFlow(null)

    private val _drawer: MutableStateFlow<(@Composable ColumnScope.(
        drawerItems: StateFlow<List<DrawerItem>?>,
        modifier: StateFlow<Modifier>,
        background: StateFlow<Color?>,
    ) -> Unit)?> = MutableStateFlow(null)

    @Composable
    fun drawer(): (@Composable ColumnScope.() -> Unit)? = _drawer.safeValue?.let {{
        it(
            _drawerItems.asStateFlow(),
            _drawerModifier.asStateFlow(),
            _drawerBackground.asStateFlow(),
        )
    }}


    fun update(change: ScaffoldChange) {
        when (change) {
            is ScaffoldChange.ScreenChange -> {
                _screen.update(change.screen)
            }
            is ScaffoldChange.TopBarChange.Title -> {
                _topBarTitle.updateNullable(change.title)
            }
            is ScaffoldChange.TopBarChange.Modify -> {
                _topBarModifier.update(change.modifier)
            }
            is ScaffoldChange.TopBarChange.AlignText -> {
                _topBarTextAlign.update(change.textAlign)
            }
            is ScaffoldChange.TopBarChange.ButtonUp -> {
                _topBarUpButton.updateNullable(change.upButton)
            }
            is ScaffoldChange.TopBarChange.MenuItems -> {
                _topBarMenuItems.updateNullable(change.menuItems)
            }
            is ScaffoldChange.TopBarChange.Elevation -> {
                _topBarElevation.updateNullable(change.elevation)
            }
            is ScaffoldChange.TopBarChange.Background -> {
                _topBarBackground.updateNullable(change.background)
            }
            is ScaffoldChange.TopBarChange.All -> {
                _topBarTitle.updateNullable(change.title)
                _topBarModifier.update(change.modifier)
                _topBarTextAlign.update(change.textAlign)
                _topBarUpButton.updateNullable(change.upButton)
                _topBarMenuItems.updateNullable(change.menuItems)
                _topBarElevation.updateNullable(change.elevation)
                _topBarBackground.updateNullable(change.background)
            }
            is ScaffoldChange.TopBarChange.Compose -> {
                _topBar.updateNullable(change.topBar)
            }
            is ScaffoldChange.TopBarChange.MaybeCompose -> {
                if (_topBar.value == null) {
                    _topBar.updateNullable(change.topBar)
                }
            }
            is ScaffoldChange.DrawerChange.DrawerItems -> {
                _drawerItems.updateNullable(change.drawerItems)
            }
            is ScaffoldChange.DrawerChange.Modify -> {
                _drawerModifier.update(change.modifier)
            }
            is ScaffoldChange.DrawerChange.Background -> {
                _drawerBackground.updateNullable(change.background)
            }
            is ScaffoldChange.DrawerChange.All -> {
                _drawerItems.updateNullable(change.drawerItems)
                _drawerModifier.update(change.modifier)
                _drawerBackground.updateNullable(change.background)
            }
            is ScaffoldChange.DrawerChange.Compose -> {
                _drawer.updateNullable(change.drawer)
            }
            is ScaffoldChange.DrawerChange.MaybeCompose -> {
                if (_drawer.value == null) {
                    _drawer.updateNullable(change.drawer)
                }
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
