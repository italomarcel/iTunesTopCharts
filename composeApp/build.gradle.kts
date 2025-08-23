plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Core Kotlin
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.collections.immutable)

            // UI - Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            // Network - Ktor 3.0
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Database - SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

            // Dependency Injection - Koin 4.0
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // Image Loading - Kamel
            implementation(libs.kamel.image)

            // Logging - Kermit
            implementation(libs.kermit)
        }

        androidMain.dependencies {
            // Android-specific network
            implementation(libs.ktor.client.okhttp)

            // Android-specific database
            implementation(libs.sqldelight.android.driver)

            // Android Compose Activity
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.viewmodel.compose)

            // Android-specific Koin
            implementation(libs.koin.android)

            // Navigation (Android only for now)
            implementation(libs.navigation.compose)
        }

        iosMain.dependencies {
            // iOS-specific network
            implementation(libs.ktor.client.darwin)

            // iOS-specific database
            implementation(libs.sqldelight.native.driver)
        }

        commonTest.dependencies {
            // Testing framework
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)

            // Network mocking
            implementation(libs.ktor.client.mock)

            // Testing utilities - Android only for now
            implementation(libs.turbine)
            implementation(libs.junit)
        }

        androidUnitTest.dependencies {
            // MockK only for Android tests
            implementation(libs.mockk)
        }
    }
}

android {
    namespace = "com.company.itunes"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("iTunesAlbumsDatabase") {
            packageName.set("com.company.itunes.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
}