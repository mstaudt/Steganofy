package com.braffdev.steganofy.ui

import android.app.Application
import com.braffdev.steganofy.ui.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SteganofyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SteganofyApplication)
            modules(appModule)
        }
    }
}