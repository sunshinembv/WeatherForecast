package com.example.weatherforecast.data.model.daily_forecast_weather

import com.example.weatherforecast.domain.model.daily_forecast_weather.Hourly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyData(
    @Json(name = "dt")
    override val date: Long,
    override val temp: Double,
    override val weather: List<WeatherData>
) : Hourly
