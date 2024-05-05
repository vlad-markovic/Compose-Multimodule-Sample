/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation

import com.vladmarkovic.sample.covid_presentation.country_comparison.CovidCountryComparisonScreenComposer
import com.vladmarkovic.sample.covid_presentation.country_info.CovidCountryInfoScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposerSelector
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/** Composer for covid info tab - selector for covid screen composers. */
@ActivityRetainedScoped
class CovidScreenComposerSelector @Inject constructor(
    private val comparisonComposer: CovidCountryComparisonScreenComposer,
    private val dataComposer: CovidCountryInfoScreenComposer
) : ScreenComposerSelector<CovidScreen> {

    override val allScreens: List<CovidScreen> = CovidScreen.entries

    override val CovidScreen.screenComposer: ScreenComposer<*>
        get() = when (this) {
            CovidScreen.COVID_COUNTRY_COMPARISON -> comparisonComposer
            CovidScreen.COVID_COUNTRY_INFO -> dataComposer
        }
}
