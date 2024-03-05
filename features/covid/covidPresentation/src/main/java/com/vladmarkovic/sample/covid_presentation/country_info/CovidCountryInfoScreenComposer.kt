/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import java.util.Optional
import javax.inject.Inject

/** Defines Compose UI and elements for country Covid details screen. */
class CovidCountryInfoScreenComposer @Inject constructor() : ScreenComposer<CovidCountryInfoViewModel>() {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_INFO

    @Composable
    override fun viewModel(args: ScreenArgs): CovidCountryInfoViewModel =
        actionViewModel<CovidCountryInfoViewModel>(args.bubbleUp)

    override fun topBarChange(args: ScreenArgs, viewModel: CovidCountryInfoViewModel): Optional<@Composable (() -> Unit)> =
        defaultTopBarChange(viewModel.info.country.str)

    @Composable
    override fun Content(args: ScreenArgs, viewModel: CovidCountryInfoViewModel) {
        CountryCovidInfoScreen(viewModel.info)
    }
}
