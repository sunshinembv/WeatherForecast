package com.example.weatherforecast.domain.usecases

import com.example.weatherforecast.domain.repository.AppUtilsRepository

class GetAppIsFirstStartUseCase(private val repository: AppUtilsRepository) {

    fun execute(key: String, def: Boolean): Boolean {
        return repository.getBoolean(key, def)
    }
}
