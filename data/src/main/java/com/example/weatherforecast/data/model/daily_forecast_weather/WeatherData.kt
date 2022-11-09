package com.example.weatherforecast.data.model.daily_forecast_weather

import com.example.weatherforecast.domain.model.daily_forecast_weather.Weather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherData(
    override val id: Int,
    override val main: String,
    override val description: String,
    override val icon: String
) : Weather
