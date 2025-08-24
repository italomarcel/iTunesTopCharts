package com.company.itunes

import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin

private var isKoinInitialized = false

fun MainViewController() = ComposeUIViewController {
    if (!isKoinInitialized) {
        initKoin()
        isKoinInitialized = true
    }
    App()
}