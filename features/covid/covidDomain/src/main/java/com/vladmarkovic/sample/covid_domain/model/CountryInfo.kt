/** Copyright (C) 2022 Vladimir Markovic - All Rights Reserved */

package com.vladmarkovic.sample.covid_domain.model

@kotlinx.serialization.Serializable
data class CountryInfo(
    val _id: Int?,
    val iso2: String?,
    val iso3: String?,
    val lat: Float,
    val long: Float,
    val flag: String
)
