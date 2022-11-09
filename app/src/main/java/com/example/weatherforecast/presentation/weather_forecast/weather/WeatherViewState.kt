package com.example.weatherforecast.presentation.weather_forecast.weather

import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.ForecastWeatherUI

sealed class WeatherViewState {
    object Loading : WeatherViewState()
    object Empty : WeatherViewState()
    data class Success(val dataUI: ForecastWeatherUI) : WeatherViewState()
    data class Error(val message: String) : WeatherViewState()
}
