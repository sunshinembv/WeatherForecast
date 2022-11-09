package com.example.weatherforecast.presentation.weather_forecast.weather.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.usecases.GetAppIsFirstStartUseCase
import com.example.weatherforecast.domain.usecases.GetDailyForecastWeatherUseCase
import com.example.weatherforecast.domain.usecases.GetLocationNameUseCase
import com.example.weatherforecast.domain.usecases.SetAppFirstStartUseCase
import com.example.weatherforecast.location.LocationClient
import com.example.weatherforecast.presentation.weather_forecast.weather.event.WeatherForecastEvent
import com.example.weatherforecast.presentation.weather_forecast.weather.WeatherViewState
import com.example.weatherforecast.presentation.weather_forecast.weather.mapper.ForecastWeatherUIMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityForecastWeatherViewModel(
    private val getDailyForecastWeatherUseCase: GetDailyForecastWeatherUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val forecastWeatherUIMapper: ForecastWeatherUIMapper,
    private val locationClient: LocationClient,
    private val getAppIsFirstStartUseCase: GetAppIsFirstStartUseCase,
    private val setAppFirstStartUseCase: SetAppFirstStartUseCase
) : ViewModel() {

    private val _forecastWeather = MutableStateFlow<WeatherViewState>(WeatherViewState.Empty)
    val forecastWeather: StateFlow<WeatherViewState> = _forecastWeather.asStateFlow()

    fun obtainEvent(event: WeatherForecastEvent) {
        when (event) {
            is WeatherForecastEvent.WeatherInitEvent -> {
                getForecastWeather(event.isPermissionGranted)
            }
            is WeatherForecastEvent.WeatherByCoordinatesFetch -> {
                getDailyForecastWeatherByCoordinates(
                    event.lat,
                    event.lon,
                    event.exclude,
                    event.units,
                    event.limit
                )
            }
            is WeatherForecastEvent.WeatherByLocationFetch -> {
                getDailyForecastWeatherByLocation()
            }
            is WeatherForecastEvent.WeatherIsAppFirstStartEvent -> saveAppFirstStart(event.isAppFirstStart)
        }
    }

    private fun getForecastWeather(isPermissionGranted: Boolean) {
        if (_forecastWeather.value is WeatherViewState.Success) return
        if (isPermissionGranted) {
            getDailyForecastWeatherByLocation()
        } else {
            getDailyForecastWeatherByCoordinates(
                TOKYO_NERIMA_LATITUDE_DEFAULT,
                TOKYO_NERIMA_LONGITUDE_DEFAULT
            )
        }
    }

    private fun getDailyForecastWeatherByCoordinates(
        lat: Double,
        lon: Double,
        exclude: String = "minutely,alerts",
        units: String = "metric",
        limit: Int = 1
    ) {
        viewModelScope.launch {
            try {
                _forecastWeather.emit(WeatherViewState.Loading)
                val isAppFirstSTart = getAppIsFirstStartUseCase.execute(APP_FIRST_START_KEY, true)
                val dailyWeather = getDailyForecastWeatherUseCase.execute(lat, lon, exclude, units)
                val locationName = getLocationNameUseCase.execute(lat, lon, limit)
                val forecastWeatherUI = forecastWeatherUIMapper.fromDailyForecastWeather(
                    isAppFirstSTart,
                    dailyWeather,
                    locationName.firstOrNull()?.name ?: ""
                )
                _forecastWeather.emit(WeatherViewState.Success(forecastWeatherUI))
            } catch (t: Throwable) {
                Log.d(TAG_ERROR, t.toString())
                _forecastWeather.emit(WeatherViewState.Error(t.toString()))
            }
        }
    }

    private fun getDailyForecastWeatherByLocation(
        exclude: String = "minutely,alerts",
        units: String = "metric",
        limit: Int = 1
    ) {
        viewModelScope.launch {
            try {
                when (val lastLocation = locationClient.getLastLocation()) {
                    null -> {
                        _forecastWeather.emit(WeatherViewState.Error("Network is disabled"))
                    }
                    else -> {
                        _forecastWeather.emit(WeatherViewState.Loading)
                        val dailyWeather = getDailyForecastWeatherUseCase.execute(
                            lastLocation.latitude,
                            lastLocation.longitude,
                            exclude,
                            units
                        )
                        val locationName = getLocationNameUseCase.execute(
                            lastLocation.latitude,
                            lastLocation.longitude, limit
                        )
                        val forecastWeatherUI =
                            forecastWeatherUIMapper.fromDailyForecastWeather(
                                false,
                                dailyWeather,
                                locationName.firstOrNull()?.name ?: ""
                            )
                        _forecastWeather.emit(WeatherViewState.Success(forecastWeatherUI))
                    }
                }
            } catch (t: Throwable) {
                Log.d(TAG_ERROR, t.toString())
                _forecastWeather.emit(WeatherViewState.Error(t.toString()))
            }
        }
    }

    private fun saveAppFirstStart(value: Boolean) {
        setAppFirstStartUseCase.execute(APP_FIRST_START_KEY, value)
        viewModelScope.launch {
            if (_forecastWeather.value is WeatherViewState.Success) {
                _forecastWeather.emit(
                    WeatherViewState.Success(
                        (_forecastWeather.value as WeatherViewState.Success).dataUI.copy(
                            isAppFirstStart = false
                        )
                    )
                )
            }
        }
    }

    class Factory @Inject constructor(
        private val getDailyForecastWeatherUseCase: GetDailyForecastWeatherUseCase,
        private val getLocationNameUseCase: GetLocationNameUseCase,
        private val forecastWeatherUIMapper: ForecastWeatherUIMapper,
        private val locationClient: LocationClient,
        private val getAppIsFirstStartUseCase: GetAppIsFirstStartUseCase,
        private val setAppFirstStartUseCase: SetAppFirstStartUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == CityForecastWeatherViewModel::class.java)
            return CityForecastWeatherViewModel(
                getDailyForecastWeatherUseCase,
                getLocationNameUseCase,
                forecastWeatherUIMapper,
                locationClient,
                getAppIsFirstStartUseCase,
                setAppFirstStartUseCase
            ) as T
        }
    }

    companion object {
        private val TAG_ERROR = CityForecastWeatherViewModel::class.java.simpleName
        private const val APP_FIRST_START_KEY = "appFirstStart"
        const val TOKYO_NERIMA_LATITUDE_DEFAULT = 35.6897
        const val TOKYO_NERIMA_LONGITUDE_DEFAULT = 139.6922
    }
}
