package com.example.weatherforecast.presentation.weather_forecast.weather.event

sealed class WeatherForecastEvent {

    class WeatherInitEvent(val isPermissionGranted: Boolean) : WeatherForecastEvent()

    class WeatherByCoordinatesFetch(
        val lat: Double,
        val lon: Double,
        val exclude: String = "minutely,alerts",
        val units: String = "metric",
        val limit: Int = 1
    ) : WeatherForecastEvent()

    class WeatherByLocationFetch(
        val exclude: String = "minutely,alerts",
        val units: String = "metric",
        val limit: Int = 1
    ) : WeatherForecastEvent()

    class WeatherIsAppFirstStartEvent(val isAppFirstStart: Boolean) : WeatherForecastEvent()
}
