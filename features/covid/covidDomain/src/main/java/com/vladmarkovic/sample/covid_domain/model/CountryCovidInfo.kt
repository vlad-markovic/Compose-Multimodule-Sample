/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_domain.model

@kotlinx.serialization.Serializable
data class CountryCovidInfo(
    val updated: Long,
    val country: String,
    val countryInfo: CountryInfo,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val todayRecovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion: Float,
    val deathsPerOneMillion: Float,
    val tests: Int,
    val testsPerOneMillion: Float,
    val population: Int,
    val continent: String,
    val oneCasePerPeople: Int,
    val oneDeathPerPeople: Int,
    val oneTestPerPeople: Int,
    val activePerOneMillion: Float,
    val recoveredPerOneMillion: Float,
    val criticalPerOneMillion: Float,
)
