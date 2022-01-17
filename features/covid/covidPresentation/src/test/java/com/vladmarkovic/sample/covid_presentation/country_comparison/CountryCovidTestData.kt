/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_domain.model.CountryInfo


val testCountryCovidInfoData = listOf(
    CountryCovidInfo(
        updated = 1638104175968,
        country = "Afghanistan",
        countryInfo = CountryInfo(
            _id = 4,
            iso2 = "AF",
            iso3 = "AFG",
            lat = 33.0f,
            long = 65.0f,
            flag = "https://disease.sh/assets/img/flags/af.png"
        ),
        cases = 157218,
        todayCases = 28,
        deaths = 7308,
        todayDeaths = 0,
        recovered = 140464,
        todayRecovered = 13,
        active = 9446,
        critical = 1124,
        casesPerOneMillion = 3915.0f,
        deathsPerOneMillion = 182.0f,
        tests = 790352,
        testsPerOneMillion = 19681.0f,
        population = 40157231,
        continent = "Asia",
        oneCasePerPeople = 255,
        oneDeathPerPeople = 5495,
        oneTestPerPeople = 51,
        activePerOneMillion = 235.23f,
        recoveredPerOneMillion = 3497.85f,
        criticalPerOneMillion = 27.99f
    ),
    CountryCovidInfo(
        updated = 1638104175960,
        country = "Albania",
        countryInfo = CountryInfo(
            _id = 8,
            iso2 = "AL",
            iso3 = "ALB",
            lat = 41.0f,
            long = 20.0f,
            flag = "https://disease.sh/assets/img/flags/al.png"
        ),
        cases = 199137,
        todayCases = 0,
        deaths = 3085,
        todayDeaths = 0,
        recovered = 188593,
        todayRecovered = 0,
        active = 7459,
        critical = 23,
        casesPerOneMillion = 69304.0f,
        deathsPerOneMillion = 1074.0f,
        tests = 1388238,
        testsPerOneMillion = 483137.0f,
        population = 2873382,
        continent = "Europe",
        oneCasePerPeople = 14,
        oneDeathPerPeople = 931,
        oneTestPerPeople = 2,
        activePerOneMillion = 2595.9f,
        recoveredPerOneMillion = 65634.5f,
        criticalPerOneMillion = 8.0f
    ),
    CountryCovidInfo(
        updated = 1638104175959,
        country = "Australia",
        countryInfo = CountryInfo(
            _id = 36,
            iso2 = "AU",
            iso3 = "AUS",
            lat = -27.0f,
            long = 133.0f,
            flag = "https://disease.sh/assets/img/flags/au.png"
        ),
        cases = 208004,
        todayCases = 1256,
        deaths = 1994,
        todayDeaths = 4,
        recovered = 190566,
        todayRecovered = 0,
        active = 15444,
        critical = 73,
        casesPerOneMillion = 8026.0f,
        deathsPerOneMillion = 77.0f,
        tests = 47498541,
        testsPerOneMillion = 1832849.0f,
        population = 25915138,
        continent = "Australia-Oceania",
        oneCasePerPeople = 125,
        oneDeathPerPeople = 12997,
        oneTestPerPeople = 1,
        activePerOneMillion = 595.95f,
        recoveredPerOneMillion = 7353.46f,
        criticalPerOneMillion = 2.82f
    )
)

val testCountryCovidItemsNotGroupedSortedByName = listOf(
    CountryComparisonItem.CountryDetails.NotGrouped(
        ordinal = 1,
        sortField = "Afghanistan",
        info = CountryCovidInfo(
            updated = 1638104175968,
            country = "Afghanistan",
            countryInfo = CountryInfo(
                _id = 4,
                iso2 = "AF",
                iso3 = "AFG",
                lat = 33.0f,
                long = 65.0f,
                flag = "https://disease.sh/assets/img/flags/af.png"
            ),
            cases = 157218,
            todayCases = 28,
            deaths = 7308,
            todayDeaths = 0,
            recovered = 140464,
            todayRecovered = 13,
            active = 9446,
            critical = 1124,
            casesPerOneMillion = 3915.0f,
            deathsPerOneMillion = 182.0f,
            tests = 790352,
            testsPerOneMillion = 19681.0f,
            population = 40157231,
            continent = "Asia",
            oneCasePerPeople = 255,
            oneDeathPerPeople = 5495,
            oneTestPerPeople = 51,
            activePerOneMillion = 235.23f,
            recoveredPerOneMillion = 3497.85f,
            criticalPerOneMillion = 27.99f
        )
    ),
    CountryComparisonItem.CountryDetails.NotGrouped(
        ordinal = 2,
        sortField = "Albania",
        info = CountryCovidInfo(
            updated = 1638104175960,
            country = "Albania",
            countryInfo = CountryInfo(
                _id = 8,
                iso2 = "AL",
                iso3 = "ALB",
                lat = 41.0f,
                long = 20.0f,
                flag = "https://disease.sh/assets/img/flags/al.png"
            ),
            cases = 199137,
            todayCases = 0,
            deaths = 3085,
            todayDeaths = 0,
            recovered = 188593,
            todayRecovered = 0,
            active = 7459,
            critical = 23,
            casesPerOneMillion = 69304.0f,
            deathsPerOneMillion = 1074.0f,
            tests = 1388238,
            testsPerOneMillion = 483137.0f,
            population = 2873382,
            continent = "Europe",
            oneCasePerPeople = 14,
            oneDeathPerPeople = 931,
            oneTestPerPeople = 2,
            activePerOneMillion = 2595.9f,
            recoveredPerOneMillion = 65634.5f,
            criticalPerOneMillion = 8.0f
        )
    ),
    CountryComparisonItem.CountryDetails.NotGrouped(
        ordinal = 3,
        sortField = "Australia",
        info = CountryCovidInfo(
            updated = 1638104175959,
            country = "Australia",
            countryInfo = CountryInfo(
                _id = 36,
                iso2 = "AU",
                iso3 = "AUS",
                lat = -27.0f,
                long = 133.0f,
                flag = "https://disease.sh/assets/img/flags/au.png"
            ),
            cases = 208004,
            todayCases = 1256,
            deaths = 1994,
            todayDeaths = 4,
            recovered = 190566,
            todayRecovered = 0,
            active = 15444,
            critical = 73,
            casesPerOneMillion = 8026.0f,
            deathsPerOneMillion = 77.0f,
            tests = 47498541,
            testsPerOneMillion = 1832849.0f,
            population = 25915138,
            continent = "Australia-Oceania",
            oneCasePerPeople = 125,
            oneDeathPerPeople = 12997,
            oneTestPerPeople = 1,
            activePerOneMillion = 595.95f,
            recoveredPerOneMillion = 7353.46f,
            criticalPerOneMillion = 2.82f
        )
    )
)
