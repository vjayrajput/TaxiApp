package com.taxi.app

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaxiApplication : MultiDexApplication() {

    companion object {

        private lateinit var myApplication: TaxiApplication

        @Synchronized
        fun getInstance(): TaxiApplication {
            return myApplication
        }

    }

    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }

}