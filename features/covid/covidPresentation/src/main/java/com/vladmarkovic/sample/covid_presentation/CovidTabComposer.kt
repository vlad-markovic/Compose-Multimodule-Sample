/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation

import com.vladmarkovic.sample.covid_presentation.country_comparison.CovidCountryComparisonScreenComposer
import com.vladmarkovic.sample.covid_presentation.country_info.CovidCountryInfoScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenManager
import com.vladmarkovic.sample.shared_presentation.composer.CurrentScreenMonitor
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenHolderType
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/** Composer for covid info tab - selector for covid screen composers. */
@ActivityRetainedScoped
class CovidTabComposer @Inject constructor(
    private val comparisonComposer: CovidCountryComparisonScreenComposer,
    private val dataComposer: CovidCountryInfoScreenComposer
) : ScreenHolderComposer<CovidScreen>,
    CurrentScreenMonitor<CovidScreen> by CurrentScreenManager(CovidScreen.entries) {

    override val type: ScreenHolderType = ScreenHolderType.TAB

    override fun composer(screen: CovidScreen): ScreenComposer =
        when (screen) {
            CovidScreen.COVID_COUNTRY_COMPARISON -> comparisonComposer
            CovidScreen.COVID_COUNTRY_INFO -> dataComposer
        }
}
