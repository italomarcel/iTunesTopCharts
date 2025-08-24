package com.company.itunes

import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import presentation.navigation.App

private var isKoinInitialized = false

fun MainViewController() = ComposeUIViewController {
    if (!isKoinInitialized) {
        initKoin()
        isKoinInitialized = true
    }
    App()
}