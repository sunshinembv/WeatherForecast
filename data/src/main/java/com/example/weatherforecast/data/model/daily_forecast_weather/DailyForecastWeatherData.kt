package com.example.weatherforecast.data.model.daily_forecast_weather

import com.example.weatherforecast.domain.model.daily_forecast_weather.DailyForecastWeather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyForecastWeatherData(
    override val current: CurrentData,
    override val hourly: List<HourlyData>,
    override val daily: List<DailyData>
) : DailyForecastWeather
