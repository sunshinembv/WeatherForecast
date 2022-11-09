package com.example.weatherforecast.domain.repository

interface AppUtilsRepository {
    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, def: Boolean): Boolean
}
