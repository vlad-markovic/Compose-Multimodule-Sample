package com.vladmarkovic.sample.shared_presentation.composer

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.vladmarkovic.sample.shared_presentation.compose.DefaultTopBar
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.ui.model.UpButton

/** Defines Compose UI and elements for a Screen. */
@Suppress("FunctionName")
abstract class ScreenComposer {

    /** Override to specify screen title. Use [titleFromRes] if using string resource. */
    abstract val screenTitle: State<StrOrRes>

    /** Override to specify up button icon and behaviour action. */
    open val upButton: State<UpButton?>? = null

    /** Override to specify a [Composable] content for this screen. */
    @Composable
    abstract fun Content(navController: NavHostController)

    @Composable
    fun TopBar() {
        DefaultTopBar(screenTitle, up = upButton)
    }

    protected fun titleFromRes(@StringRes res: Int): MutableState<StrOrRes> =
        mutableStateOf(StrOrRes.res(res))
}
