/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.vladmarkovic.sample.shared_presentation.composer.BackScreenComposer
import com.vladmarkovic.sample.shared_presentation.composer.ContentArgs
import com.vladmarkovic.sample.shared_presentation.model.StrOrRes
import com.vladmarkovic.sample.shared_presentation.screen.MainScreen.CovidScreen
import com.vladmarkovic.sample.shared_presentation.screen.Screen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModel
import com.vladmarkovic.sample.shared_presentation.util.str
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/** Defines Compose UI and elements for country Covid details screen. */
class CovidCountryInfoScreenComposer @Inject constructor() : BackScreenComposer() {

    override val screen: Screen = CovidScreen.COVID_COUNTRY_INFO

    override val screenTitle: MutableStateFlow<StrOrRes> = titleFromStr("")

    @Composable
    override fun Content(contentArgs: ContentArgs) {
        super.Content(contentArgs)

        // When a parent is injecting ViewModel with hiltViewModel, it has to called in child too,
        // so actionViewModel<BriefActionViewModel>(contentArgs) has to be called inside child Content,
        // otherwise the app crashes with "Compose Runtime internal error. Unexpected or incorrect use of
        // the Compose internal runtime API (Cannot seek outside the current group (106-1394))."
        val viewModel = actionViewModel<CovidCountryInfoViewModel>(contentArgs)

        LaunchedEffect(contentArgs) { screenTitle.value = viewModel.info.country.str }

        CountryCovidInfoScreen(viewModel.info)
    }
}
