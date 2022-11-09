package com.example.weatherforecast.presentation.weather_forecast.weather.ui_model

data class DailyForecastWeatherItemUI(
    val date: String,
    val dayOfWeek: String,
    val icon: String,
    val dayDegrees: String,
    val nightDegrees: String,
)
