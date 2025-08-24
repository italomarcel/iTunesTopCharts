# iTunesTopCharts

Kotlin Multiplatform (KMP) app to retrieve and save top charts from iTunes.

## Requirements

- **Android Studio**: Giraffe (2022.3.1) or newer
- **Kotlin**: 1.9.0 or newer
- **Xcode**: 14.1 or newer
- **Gradle**: 8.0 or newer

## Architecture

This project uses a shared KMP module for business logic, networking, and data storage, following Clean Architecture principles in the business logic layer.
- **Presentation**: Jetpack Compose (Android), SwiftUI (iOS)
- **Dependency Injection**: Koin
- **Database**: SQLDelight
- **Networking**: Ktor Client
- **Business Logic**: Clean Architecture (domain, data, presentation separation)

## Running the Project

### Android

1. Open the project in Android Studio.
2. Select the `composeApp` run configuration.
3. Click **Run**.

### iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode.
2. Select a simulator or device.
3. Build and run.

> **Note:** No need to run `pod install` if you are not using CocoaPods.

## SQLDelight

SQLDelight Gradle tasks generate database code automatically.  
To generate or update database code, run:

```sh
./gradlew generateSqlDelightInterface
