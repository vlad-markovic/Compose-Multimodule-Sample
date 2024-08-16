/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.navigation

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_domain.screen.MainScreen.CovidScreen.COVID_COUNTRY_INFO
import com.vladmarkovic.sample.common.navigation.screen.navcomponent.model.ToNavGraphScreen
import com.vladmarkovic.sample.shared_presentation.screen.ScreenArgNames
import kotlinx.serialization.json.Json

data class ToCountryInfoScreen(val countryCovidInfo: CountryCovidInfo) : ToNavGraphScreen(
    COVID_COUNTRY_INFO,
    ScreenArgNames.COUNTRY_INFO.name to Json.encodeToString(CountryCovidInfo.serializer(), countryCovidInfo)
)
