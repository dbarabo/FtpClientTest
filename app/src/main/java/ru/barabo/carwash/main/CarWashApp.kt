package ru.barabo.carwash.main

import android.app.Application

class CarWashApp : Application() {

    companion object {
        lateinit var instance: CarWashApp
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}