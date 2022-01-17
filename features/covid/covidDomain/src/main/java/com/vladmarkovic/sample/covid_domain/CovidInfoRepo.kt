/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_domain

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo

interface CovidInfoRepo {
    suspend fun getAffectedCountries(): List<CountryCovidInfo>
}
