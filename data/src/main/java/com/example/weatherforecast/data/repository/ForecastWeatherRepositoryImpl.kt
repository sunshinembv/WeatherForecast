package com.example.weatherforecast.data.repository

import com.example.weatherforecast.data.network.NetworkHandleService
import com.example.weatherforecast.data.network.WeatherForecastApi
import com.example.weatherforecast.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.weatherforecast.domain.model.geo.Coordinates
import com.example.weatherforecast.domain.model.geo.LocationName
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository
import javax.inject.Inject

class ForecastWeatherRepositoryImpl @Inject constructor(
    private val weatherForecastApi: WeatherForecastApi,
    private val networkHandleService: NetworkHandleService
) : ForecastWeatherRepository {

    override suspend fun getDailyForecastWeatherDataByCoordinates(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String
    ): DailyForecastWeather {
        return networkHandleService.apiCall {
            weatherForecastApi.getDailyForecastWeatherByCoordinates(
                lat,
                lon,
                exclude,
                units
            )
        }
    }

    override suspend fun getCoordinatesByLocationName(
        locationName: String,
        limit: Int
    ): List<Coordinates> {
        return networkHandleService.apiCall {
            weatherForecastApi.getCoordinatesByLocationName(
                locationName,
                limit
            )
        }
    }

    override suspend fun getLocationNameByCoordinates(
        lat: Double,
        lon: Double,
        limit: Int
    ): List<LocationName> {
        return networkHandleService.apiCall {
            weatherForecastApi.getLocationNameByCoordinates(
                lat,
                lon,
                limit
            )
        }
    }
}
