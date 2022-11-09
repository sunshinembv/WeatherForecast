package com.example.weatherforecast.di

import com.example.weatherforecast.data.repository.AppUtilsRepositoryImpl
import com.example.weatherforecast.data.repository.ForecastWeatherRepositoryImpl
import com.example.weatherforecast.domain.repository.AppUtilsRepository
import com.example.weatherforecast.domain.repository.ForecastWeatherRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun provideForecastWeatherRepositoryImpl(impl: ForecastWeatherRepositoryImpl): ForecastWeatherRepository

    @Binds
    fun provideAppUtilsRepositoryImpl(impl: AppUtilsRepositoryImpl): AppUtilsRepository
}
