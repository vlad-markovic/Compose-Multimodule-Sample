/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_data

import com.vladmarkovic.sample.covid_domain.CovidInfoRepo
import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.shared_data.util.unpack
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** [CovidInfoRepo] implementation for fetching a list of [CountryCovidInfo] from the api using ktor. */
class CovidInfoApiService @Inject constructor(private val httpClient: HttpClient) : CovidInfoRepo {

    companion object {
        private const val URL_COVID_INFO = "https://disease.sh/v3/covid-19/countries"
    }

    override suspend fun getAffectedCountries(): List<CountryCovidInfo> =
        withContext(Dispatchers.IO) {
            httpClient.get(URL_COVID_INFO).unpack()
        }
}
