/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_presentation.country_comparison

import com.vladmarkovic.sample.covid_domain.model.CountryCovidInfo
import kotlinx.serialization.Serializable

@Serializable
sealed class CountryComparisonItem {

    data class GroupHeader(val continent: String) : CountryComparisonItem()

    sealed class CountryDetails(
        open val ordinal: Int,
        open val sortField: String,
        open val info: CountryCovidInfo
    ) : CountryComparisonItem() {

        data class NotGrouped(
            override val ordinal: Int,
            override val sortField: String,
            override val info: CountryCovidInfo
        ) : CountryDetails(ordinal, sortField, info)

        data class Grouped(
            val inContinentOrdinal: Int,
            override val ordinal: Int,
            override val sortField: String,
            override val info: CountryCovidInfo
        ) : CountryDetails(ordinal, sortField, info)
    }
}
