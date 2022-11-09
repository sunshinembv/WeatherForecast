package com.example.weatherforecast.data.model.daily_forecast_weather

import com.example.weatherforecast.domain.model.daily_forecast_weather.Current
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentData(
    @Json(name = "dt")
    override val date: Long,
    override val temp: Double,
    @Json(name = "feels_like")
    override val feelsLike: Double,
    override val pressure: Int,
    override val humidity: Int,
    @Json(name = "wind_speed")
    override val windSpeed: Double,
    override val weather: List<WeatherData>
) : Current
