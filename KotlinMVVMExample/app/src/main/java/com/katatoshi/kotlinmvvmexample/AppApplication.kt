package com.katatoshi.kotlinmvvmexample

import android.app.Application
import nl.komponents.kovenant.android.startKovenant
import nl.komponents.kovenant.android.stopKovenant

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKovenant()
    }

    override fun onTerminate() {
        super.onTerminate()

        stopKovenant()
    }
}