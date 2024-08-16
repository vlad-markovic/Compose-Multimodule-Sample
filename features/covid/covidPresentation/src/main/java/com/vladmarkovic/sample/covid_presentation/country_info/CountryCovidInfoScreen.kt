/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

@file:Suppress("FunctionName")

package com.vladmarkovic.sample.covid_presentation.country_info

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vladmarkovic.sample.common.compose.util.padding
import com.vladmarkovic.sample.common.mv.action.compose.actionViewModel
import com.vladmarkovic.sample.common.mv.action.ViewAction
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_presentation.compose.navscaffold.components.DefaultTopBar
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.TopBarData
import com.vladmarkovic.sample.common.navigation.screen.compose.navscaffold.model.UpButton
import com.vladmarkovic.sample.shared_presentation.ui.theme.Dimens
import com.vladmarkovic.sample.common.android.model.str
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@Composable
fun CountryCovidInfoScreen(
    bubbleUp: (ViewAction) -> Unit,
    viewModel: CovidCountryInfoViewModel = actionViewModel<CovidCountryInfoViewModel>(bubbleUp)
) {
    Column(Modifier.fillMaxSize()) {
        DefaultTopBar(TopBarData(viewModel.info.country.str, UpButton.BackButton(viewModel)))

        CountryCovidInfoScreen(viewModel.info)
    }
}

@Composable
private fun CountryCovidInfoScreen(info: CountryCovidInfo) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.m)
            .scrollable(scrollState, Orientation.Vertical)
    ) {
        Detail("Date: ", SimpleDateFormat.getDateInstance().format(info.updated))
        Detail("Country: ", info.country)
        Detail("Continent: ", info.continent)
        Detail("Population: ", "${(info.population / 1000000f).roundToInt()}M")
        Detail("Cases: ", "${info.cases} (/1M: ${info.casesPerOneMillion})")
        Detail("Active cases: ", "${info.active} (/1M: ${info.activePerOneMillion})")
        Detail("Critical cases: ", "${info.critical} (/1M: ${info.criticalPerOneMillion})")
        Detail("Tests: ", "${info.tests} (/1M: ${info.testsPerOneMillion})")
        Detail("Recovered: ", "${info.recovered} (/1M: ${info.recoveredPerOneMillion})")
        Detail("Deaths: ", "${info.deaths} (/1M: ${info.deathsPerOneMillion})")
        Detail("Today cases: ", "${info.todayCases}")
        Detail("Today recovered: ", "${info.todayRecovered}")
        Detail("Today deaths: ", "${info.todayDeaths}")
    }
}

@Composable
private fun Detail(caption: String, text: String) {
    Row(Modifier.padding(bottom = Dimens.m)) {
        Text(text = caption, modifier = Modifier.width(150.dp))
        Text(text)
    }
}
