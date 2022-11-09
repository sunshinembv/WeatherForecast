package com.example.weatherforecast.presentation.weather_forecast.search_city

import com.example.weatherforecast.presentation.weather_forecast.search_city.ui_model.SearchCityItemUI

sealed class SearchCityViewState {
    object Loading : SearchCityViewState()
    object Empty : SearchCityViewState()
    data class Success(val dataUI: List<SearchCityItemUI>) : SearchCityViewState()
    data class Error(val message: String) : SearchCityViewState()
}
