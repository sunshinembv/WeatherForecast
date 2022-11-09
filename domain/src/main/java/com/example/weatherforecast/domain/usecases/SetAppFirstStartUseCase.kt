package com.example.weatherforecast.domain.usecases

import com.example.weatherforecast.domain.repository.AppUtilsRepository

class SetAppFirstStartUseCase(private val repository: AppUtilsRepository) {

    fun execute(key: String, value: Boolean) {
        repository.setBoolean(key, value)
    }
}
