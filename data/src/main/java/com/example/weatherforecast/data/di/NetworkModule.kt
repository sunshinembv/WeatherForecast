package com.example.weatherforecast.data.di

import com.example.weatherforecast.data.network.WeatherForecastApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideWeatherForecastApi(): WeatherForecastApi {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).addInterceptor { chain ->
            val modifiedRequest =
                chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
            chain.proceed(modifiedRequest)
        }.build()

        val retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        return retrofit.create()
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"
        private const val HEADER_API_KEY = "X-Api-Key"
        private const val API_KEY = "875f91518b7aa12e6481c305fdd039f4"
    }
}
