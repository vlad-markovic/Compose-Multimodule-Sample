/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.compose.runtime.Composable
import com.vladmarkovic.sample.shared_presentation.compose.ScaffoldChange
import com.vladmarkovic.sample.shared_presentation.composer.StackContentArgs
import com.vladmarkovic.sample.shared_presentation.composer.TabScreenComposer
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import javax.inject.Inject

/** Defines Compose UI and elements for country Covid details screen. */
class CovidCountryInfoScreenComposer @Inject constructor() : TabScreenComposer<CovidCountryInfoViewModel> {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_INFO

    @Composable
    override fun viewModel(stackContentArgs: StackContentArgs): CovidCountryInfoViewModel =
        actionViewModel<CovidCountryInfoViewModel>(stackContentArgs.bubbleUp)

    @Composable
    override fun Content(
        stackContentArgs: StackContentArgs,
        screenSetup: (ScaffoldChange) -> Unit,
        viewModel: CovidCountryInfoViewModel
    ) {
        super.Content(stackContentArgs, screenSetup, viewModel)

        SetupScreen(
            screenSetup,
            change(title = viewModel.info.country.str)
        )

        CountryCovidInfoScreen(viewModel.info)
    }
}
