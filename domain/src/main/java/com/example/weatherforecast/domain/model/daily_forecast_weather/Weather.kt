package com.example.weatherforecast.domain.model.daily_forecast_weather

interface Weather {
    val id: Int
    val main: String
    val description: String
    val icon: String
}
