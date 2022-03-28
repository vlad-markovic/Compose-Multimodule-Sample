/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_data

import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

/** [CovidInfoRepo] implementation for fetching a list of [CountryCovidInfo] from the api using ktor. */
class CovidInfoApiService @Inject constructor(private val httpClient: HttpClient) : CovidInfoRepo {

    companion object {
        private const val URL_COVID_INFO = "https://disease.sh/v3/covid-19/countries"
    }

    override suspend fun getAffectedCountries(): List<CountryCovidInfo> =
        httpClient.get(URL_COVID_INFO)
}
