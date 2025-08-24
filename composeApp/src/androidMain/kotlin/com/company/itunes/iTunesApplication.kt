package com.company.itunes

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext

class iTunesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjection()
    }

    private fun initializeDependencyInjection() {
        initKoin {
            androidContext(this@iTunesApplication)
        }
    }
}