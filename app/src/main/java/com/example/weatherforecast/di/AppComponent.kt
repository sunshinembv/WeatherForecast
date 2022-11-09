package com.example.weatherforecast.di

import android.content.Context
import com.example.weatherforecast.data.di.NetworkModule
import com.example.weatherforecast.presentation.weather_forecast.search_city.fragment.SearchCityFragment
import com.example.weatherforecast.presentation.weather_forecast.weather.fragment.CityForecastWeatherFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        ForecastWeatherUseCaseModule::class,
        RepositoryModule::class,
        LocationModule::class,
        SharedPreferencesModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(cityForecastWeatherFragment: CityForecastWeatherFragment)
    fun inject(searchCityFragment: SearchCityFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
