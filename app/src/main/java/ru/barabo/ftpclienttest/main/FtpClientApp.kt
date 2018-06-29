package ru.barabo.ftpclienttest.main

import android.app.Application

class FtpClientApp : Application() {

    companion object {
        lateinit var instance: FtpClientApp
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}