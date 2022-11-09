package com.example.weatherforecast.domain.model.daily_forecast_weather

interface Hourly {
    val date: Long
    val temp: Double
    val weather: List<Weather>
}
