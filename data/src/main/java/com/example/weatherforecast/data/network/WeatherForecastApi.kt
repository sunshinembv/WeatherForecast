package com.example.weatherforecast.data.network

import com.example.weatherforecast.data.model.daily_forecast_weather.DailyForecastWeatherData
import com.example.weatherforecast.data.model.geo.CoordinatesData
import com.example.weatherforecast.data.model.geo.LocationNameData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastApi {

    @GET("data/2.5/onecall?")
    suspend fun getDailyForecastWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Response<DailyForecastWeatherData>

    @GET("geo/1.0/direct?")
    suspend fun getCoordinatesByLocationName(
        @Query("q") locationName: String,
        @Query("limit") limit: Int,
    ): Response<List<CoordinatesData>>

    @GET("geo/1.0/reverse?")
    suspend fun getLocationNameByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int,
    ): Response<List<LocationNameData>>
}
