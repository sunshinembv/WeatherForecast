package com.example.weatherforecast.presentation.weather_forecast.search_city.mapper

import com.example.weatherforecast.domain.model.geo.Coordinates
import com.example.weatherforecast.presentation.weather_forecast.search_city.ui_model.SearchCityItemUI
import javax.inject.Inject

class SearchCityMapper @Inject constructor() {

    fun fromCoordinates(coordinates: List<Coordinates>): List<SearchCityItemUI> {
        return coordinates.map { SearchCityItemUI(it.lat, it.lon, it.name, it.country, it.state) }
    }
}
