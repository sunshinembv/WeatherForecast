package com.example.weatherforecast.presentation.weather_forecast.weather.ui_model

data class ForecastWeatherUI(
    val isAppFirstStart: Boolean,
    val locationName: String,
    val degrees: String,
    val description: String,
    val feelsLike: String,
    val wind: String,
    val pressure: String,
    val humidity: String,
    val icon: String,
    val dailyDailyForecast: List<DailyForecastWeatherItemUI>,
    val hourlyDailyForecast: List<HourlyForecastWeatherItemUI>,
)
