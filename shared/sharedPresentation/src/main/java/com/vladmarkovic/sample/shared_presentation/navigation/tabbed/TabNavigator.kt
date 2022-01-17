package com.vladmarkovic.sample.shared_presentation.navigation.tabbed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladmarkovic.sample.shared_presentation.navigation.Tab
import com.vladmarkovic.sample.shared_presentation.screen.Screen

/** Manages tab navigation on tab press. State-full (uses LiveData) to keep track of current tab. */
class TabNavigator<S: Screen, T: Tab<S>> constructor(initialTab: T)  {

    private val _tab = MutableLiveData(initialTab)
    val tab: LiveData<T> = _tab

    fun navigateTo(tab: T) {
        _tab.value = tab
    }
}
