package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.di.AppComponent
import com.example.weatherforecast.di.DaggerAppComponent

class MainApp : Application() {

    private var _appComponent: AppComponent? = null

    internal val appComponent: AppComponent
        get() = checkNotNull(_appComponent)

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder().context(this).build()
    }
}
