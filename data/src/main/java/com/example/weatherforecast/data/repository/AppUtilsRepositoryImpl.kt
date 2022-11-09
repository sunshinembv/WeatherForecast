package com.example.weatherforecast.data.repository

import android.content.SharedPreferences
import com.example.weatherforecast.domain.repository.AppUtilsRepository
import javax.inject.Inject

class AppUtilsRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : AppUtilsRepository {
    override fun setBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, def: Boolean): Boolean = sharedPrefs.getBoolean(key, def)
}
