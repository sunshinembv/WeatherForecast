package com.example.weatherforecast.data.model.geo

import com.example.weatherforecast.domain.model.geo.LocationName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationNameData(override val name: String) : LocationName
