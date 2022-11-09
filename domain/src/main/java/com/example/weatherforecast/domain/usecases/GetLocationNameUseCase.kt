package com.example.weatherforecast.domain.usecases

import com.example.weatherforecast.domain.model.geo.LocationName
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository

class GetLocationNameUseCase(private val repository: ForecastWeatherRepository) {

    suspend fun execute(lat: Double, lon: Double, limit: Int): List<LocationName> {
        return repository.getLocationNameByCoordinates(lat, lon, limit)
    }
}
