package com.example.weatherforecast.di

import com.example.weatherforecast.domain.repository.AppUtilsRepository
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository
import com.example.weatherforecast.domain.usecases.GetAppIsFirstStartUseCase
import com.example.weatherforecast.domain.usecases.GetCoordinatesUseCase
import com.example.weatherforecast.domain.usecases.GetDailyForecastWeatherUseCase
import com.example.weatherforecast.domain.usecases.GetLocationNameUseCase
import com.example.weatherforecast.domain.usecases.SetAppFirstStartUseCase
import dagger.Module
import dagger.Provides

@Module
class ForecastWeatherUseCaseModule {

    @Provides
    fun provideGetDailyForecastWeatherUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetDailyForecastWeatherUseCase {
        return GetDailyForecastWeatherUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetLocationNameUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetLocationNameUseCase {
        return GetLocationNameUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetCoordinatesUseCase(forecastWeatherRepository: ForecastWeatherRepository): GetCoordinatesUseCase {
        return GetCoordinatesUseCase(forecastWeatherRepository)
    }

    @Provides
    fun provideGetAppIsFirstStartUseCase(appUtilsRepositoryImpl: AppUtilsRepository): GetAppIsFirstStartUseCase {
        return GetAppIsFirstStartUseCase(appUtilsRepositoryImpl)
    }

    @Provides
    fun provideSetAppFirstStartUseCase(appUtilsRepositoryImpl: AppUtilsRepository): SetAppFirstStartUseCase {
        return SetAppFirstStartUseCase(appUtilsRepositoryImpl)
    }

}
