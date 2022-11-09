package com.example.weatherforecast.domain.usecases

import com.example.weatherforecast.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository

class GetDailyForecastWeatherUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ): DailyForecastWeather {
        return repository.getDailyForecastWeatherDataByCoordinates(lat, lon, exclude, units)
    }
}
