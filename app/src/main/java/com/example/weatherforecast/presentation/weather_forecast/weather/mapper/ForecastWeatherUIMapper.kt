package com.example.weatherforecast.presentation.weather_forecast.weather.mapper

import com.example.weatherforecast.domain.model.daily_forecast_weather.Daily
import com.example.weatherforecast.domain.model.daily_forecast_weather.DailyForecastWeather
import com.example.weatherforecast.domain.model.daily_forecast_weather.Hourly
import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.DailyForecastWeatherItemUI
import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.ForecastWeatherUI
import com.example.weatherforecast.presentation.weather_forecast.weather.ui_model.HourlyForecastWeatherItemUI
import com.example.weatherforecast.utils.pressureMbToMMhg
import com.example.weatherforecast.utils.toDateOrTime
import javax.inject.Inject

class ForecastWeatherUIMapper @Inject constructor() {

    fun fromDailyForecastWeather(
        isAppFirstStart: Boolean,
        dailyForecastWeather: DailyForecastWeather,
        locationName: String,
        datePattern: String = "dd MMMM",
        dayOfWeekPattern: String = "EEEE",
        timePattern: String = "HH:mm"
    ): ForecastWeatherUI {
        return ForecastWeatherUI(
            isAppFirstStart,
            locationName,
            dailyForecastWeather.current.temp.toInt().toString(),
            dailyForecastWeather.current.weather.first().description.replaceFirstChar { it.uppercase() },
            dailyForecastWeather.current.feelsLike.toInt().toString(),
            dailyForecastWeather.current.windSpeed.toString(),
            dailyForecastWeather.current.pressure.pressureMbToMMhg().toString(),
            dailyForecastWeather.current.humidity.toString(),
            dailyForecastWeather.current.weather.first().icon,
            dailyForecastWeather.daily.map { daily ->
                fromDaily(
                    daily,
                    datePattern,
                    dayOfWeekPattern
                )
            },
            dailyForecastWeather.hourly.map { hourly ->
                fromHourly(
                    hourly,
                    timePattern
                )
            }
        )
    }

    private fun fromDaily(
        daily: Daily,
        datePattern: String,
        dayOfWeekPattern: String
    ): DailyForecastWeatherItemUI {
        return DailyForecastWeatherItemUI(
            daily.date.toDateOrTime(datePattern),
            daily.date.toDateOrTime(dayOfWeekPattern).replaceFirstChar { it.uppercase() },
            daily.weather.first().icon,
            daily.temp.day.toInt().toString(),
            daily.temp.night.toInt().toString(),
        )
    }

    private fun fromHourly(
        hourly: Hourly,
        timePattern: String
    ): HourlyForecastWeatherItemUI {
        return HourlyForecastWeatherItemUI(
            hourly.date.toDateOrTime(timePattern),
            hourly.weather.first().icon,
            hourly.temp.toInt().toString()
        )
    }
}
