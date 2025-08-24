package com.company.itunes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.getOrNull

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        initializeDependencyInjection()

        setContent {
            App()
        }
    }

    private fun initializeDependencyInjection() {
        if (getOrNull() == null) {
            initKoin {
                androidContext(applicationContext)
            }
        }
    }
}