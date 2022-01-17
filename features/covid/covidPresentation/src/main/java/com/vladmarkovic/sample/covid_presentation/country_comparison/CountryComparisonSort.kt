/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

enum class CountryComparisonSort(val display: String) {
    COUNTRY_NAME("Country Name"), 
    TOTAL("Total Cases"), 
    AVG_TOTAL("Total Cases per 1M"), 
    ACTIVE("Active Cases"), 
    AVG_ACTIVE("Active Cases per 1M"), 
    CRITICAL("Critical Cases"),
    AVG_CRITICAL("Critical Cases per 1M"),
    RECOVERED("Recovered"),
    AVG_RECOVERED("Recovered per 1M"),
    DEATHS("Deaths"),
    AVG_DEATHS("Deaths per 1M"),
    TODAY("Today Cases"),
    TODAY_RECOVERED("Today Recovered"),
    TODAY_DEATHS("Today Deaths"),
}
