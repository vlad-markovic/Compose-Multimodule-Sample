/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_info

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails.Grouped
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.CountryDetails.NotGrouped
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonItem.GroupHeader
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort
import com.vladmarkovic.sample.covid_presentation.country_comparison.CountryComparisonSort.*
import com.vladmarkovic.sample.core.kotlin.sortedBy

fun List<CountryCovidInfo>.mapToItems(
    groupByContinent: Boolean,
    sortBy: CountryComparisonSort,
    ascending: Boolean
): List<CountryComparisonItem> =
    if (groupByContinent) {
        sortBy(sortBy, ascending)
            .mapIndexed { index, info -> InfoAndSortedOrdinal(info, index + 1) }
            .groupBy { it.info.continent }
            .map { continentAndInfoOrdinalListMap ->
                mutableListOf<CountryComparisonItem>(
                    // Add continent header
                    GroupHeader(continentAndInfoOrdinalListMap.key),
                ).apply {
                    // Add countries for the continent
                    addAll(continentAndInfoOrdinalListMap.value
                        .mapIndexed { inContinentIndex, infoAndSortedOrdinal ->
                            Grouped(
                                ordinal = infoAndSortedOrdinal.ordinal,
                                inContinentOrdinal = inContinentIndex + 1,
                                sortField = infoAndSortedOrdinal.info.sortField(sortBy),
                                info = infoAndSortedOrdinal.info
                            )
                        }
                    )
                }
            }
            .flatten()
    } else {
        sortBy(sortBy, ascending)
            .mapIndexed { index, info ->
                NotGrouped(
                    ordinal = index + 1,
                    sortField = info.sortField(sortBy),
                    info = info
                )
            }
    }

private data class InfoAndSortedOrdinal(val info: CountryCovidInfo, val ordinal: Int)

private fun List<CountryCovidInfo>.sortBy(
    sort: CountryComparisonSort,
    ascending: Boolean
): List<CountryCovidInfo> =
    when (sort) {
        COUNTRY_NAME -> sortedBy(ascending) { it.country }
        TOTAL -> sortedBy(ascending) { it.cases }
        AVG_TOTAL -> sortedBy(ascending) { it.casesPerOneMillion }
        ACTIVE -> sortedBy(ascending) { it.active }
        AVG_ACTIVE -> sortedBy(ascending) { it.activePerOneMillion }
        CRITICAL -> sortedBy(ascending) { it.critical }
        AVG_CRITICAL -> sortedBy(ascending) { it.criticalPerOneMillion }
        RECOVERED -> sortedBy(ascending) { it.recovered }
        AVG_RECOVERED -> sortedBy(ascending) { it.recoveredPerOneMillion }
        DEATHS -> sortedBy(ascending) { it.deaths }
        AVG_DEATHS -> sortedBy(ascending) { it.deathsPerOneMillion }
        TODAY -> sortedBy(ascending) { it.todayCases }
        TODAY_RECOVERED -> sortedBy(ascending) { it.todayRecovered }
        TODAY_DEATHS -> sortedBy(ascending) { it.todayDeaths }
    }

private fun CountryCovidInfo.sortField(sort: CountryComparisonSort): String =
    when (sort) {
        COUNTRY_NAME -> country
        TOTAL -> cases
        AVG_TOTAL -> casesPerOneMillion
        ACTIVE -> active
        AVG_ACTIVE -> activePerOneMillion
        CRITICAL -> critical
        AVG_CRITICAL -> criticalPerOneMillion
        RECOVERED -> recovered
        AVG_RECOVERED -> recoveredPerOneMillion
        DEATHS -> deaths
        AVG_DEATHS -> deathsPerOneMillion
        TODAY -> todayCases
        TODAY_RECOVERED -> todayRecovered
        TODAY_DEATHS -> todayDeaths
    }.toString()
