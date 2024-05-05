/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.navigation

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_presentation.navigation.ToScreen
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.CovidScreen.COVID_COUNTRY_INFO
import kotlinx.serialization.json.Json

data class ToCountryInfoScreen(val countryCovidInfo: CountryCovidInfo) : ToScreen(
    COVID_COUNTRY_INFO,
    listOf(Json.encodeToString(CountryCovidInfo.serializer(), countryCovidInfo))
)
