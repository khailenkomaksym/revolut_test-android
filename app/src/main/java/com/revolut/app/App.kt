package com.revolut.app

import android.app.Application
import com.revolut.app.di.components.AppComponent
import com.revolut.app.di.components.DaggerAppComponent
import com.revolut.app.di.modules.NetworkModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

}