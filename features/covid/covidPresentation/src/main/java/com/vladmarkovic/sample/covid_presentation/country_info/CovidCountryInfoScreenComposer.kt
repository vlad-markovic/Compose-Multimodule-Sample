/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.composer.ScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ScreenArgs
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import javax.inject.Inject

/** Defines Compose UI and elements for country Covid details screen. */
class CovidCountryInfoScreenComposer @Inject constructor() : ScreenComposer<CovidCountryInfoViewModel> {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_INFO

    @Composable
    override fun viewModel(args: ScreenArgs): CovidCountryInfoViewModel =
        actionViewModel<CovidCountryInfoViewModel>(args.bubbleUp)

    @Composable
    override fun Content(args: ScreenArgs, viewModel: CovidCountryInfoViewModel) {
        super.Content(args, viewModel)

        SetupScreen(
            args.screenSetup,
            change(title = viewModel.info.country.str)
        )

        CountryCovidInfoScreen(viewModel.info)
    }
}
