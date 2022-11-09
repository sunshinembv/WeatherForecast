package com.example.weatherforecast.domain.model.daily_forecast_weather

interface Daily {
    val date: Long
    val temp: Temperature
    val weather: List<Weather>
}
