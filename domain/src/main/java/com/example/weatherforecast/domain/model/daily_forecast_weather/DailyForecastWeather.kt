package com.example.weatherforecast.domain.model.daily_forecast_weather

interface DailyForecastWeather {
    val current: Current
    val hourly: List<Hourly>
    val daily: List<Daily>
}
