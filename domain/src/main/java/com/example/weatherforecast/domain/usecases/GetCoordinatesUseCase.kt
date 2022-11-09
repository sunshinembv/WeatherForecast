package com.example.weatherforecast.domain.usecases

import com.example.weatherforecast.domain.model.geo.Coordinates
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository

class GetCoordinatesUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(locationName: String, limit: Int): List<Coordinates> {
        return repository.getCoordinatesByLocationName(locationName, limit)
    }
}
