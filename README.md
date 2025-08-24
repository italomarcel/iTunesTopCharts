# iTunesTopCharts

A Kotlin Multiplatform app that displays the top music albums from iTunes, with offline support and search functionality.

## Features

- 📱 **Cross-platform**: Shared business logic between Android and iOS
- 🎵 **iTunes Integration**: Fetches real-time top 100 albums 
- 💾 **Offline Support**: Local database with SQLDelight
- 🔍 **Search & Filter**: Find albums by name or artist
- 🔄 **Pull to Refresh**: Always get the latest charts
- 🎨 **Native UI**: Jetpack Compose for Android, SwiftUI for iOS

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Shared Logic** | Kotlin Multiplatform |
| **Android UI** | Jetpack Compose |
| **iOS UI** | SwiftUI |
| **Networking** | Ktor Client |
| **Database** | SQLDelight |
| **DI** | Koin |
| **Architecture** | Clean Architecture + MVVM |

## Requirements

- **Android Studio**: 2024.1.1 (Koala) or newer
- **Kotlin**: 2.1.0
- **Xcode**: 15.0 or newer (for iOS development)
- **Gradle**: 8.7 or newer
- **iOS Deployment Target**: 14.0+
- **Android API Level**: 24+

## Project Structure

```
composeApp/
├── commonMain/          # Shared Kotlin code
│   ├── data/           # Repositories, API, Database
│   ├── di/             # Injections and modules
│   ├── domain/         # Business logic, Use cases
│   └── presentation/   # ViewModels, UI State
├── androidMain/        # Android-specific code
└── iosMain/           # iOS-specific code

iosApp/                 # iOS Xcode project
└── iosApp.xcodeproj
```

## Getting Started

### Android Setup

1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Select `composeApp` configuration
5. Run on device or emulator

### iOS Setup

1. Ensure you have Xcode installed
2. Open `iosApp/iosApp.xcodeproj` in Xcode
3. Set your Development Team in project settings
4. Select target device/simulator
5. Build and run

## Development

### Database Schema Updates

When modifying SQLDelight schemas:
```bash
./gradlew generateSqlDelightInterface
```

### Clean Build
```bash
./gradlew clean
./gradlew cleanBuildCache
```

## API Integration

The app integrates with iTunes Search API:
- **Endpoint**: `https://itunes.apple.com/us/rss/topalbums/limit=100/json`
- **Format**: JSON with nested structure
- **Rate Limiting**: Handled with retry mechanisms
- **Timeout**: 30 seconds with exponential backoff

## Troubleshooting

### iOS Build Issues

**SQLite Linker Errors:**
- Ensure `-lsqlite3` is in linker flags
- Clean derived data: `Product → Clean Build Folder`

**Memory Issues:**
```bash
# Increase Gradle memory
./gradlew -Xmx8g :composeApp:linkDebugFrameworkIosArm64
```

### Android Build Issues

**OutOfMemoryError:**
Add to `gradle.properties`:
```
org.gradle.jvmargs=-Xmx8192m
```

## Contributing

1. Fork the project
2. Create your feature branch
3. Follow Kotlin coding conventions
4. Ensure tests pass
5. Submit a pull request

## Architecture Decisions

- **Clean Architecture**: Separation of concerns with clear dependency rules
- **Repository Pattern**: Abstracts data sources (API + Database)
- **Unidirectional Data Flow**: MVVM with reactive streams
- **Error Handling**: Typed errors with user-friendly messages
- **Coroutine Safety**: Proper cancellation handling with `runSuspendCatching`

## License

This project is for educational purposes.
