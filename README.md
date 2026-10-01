# Horoscope Android

Kotlin and Jetpack Compose training project for building a horoscope app step by step.

## Current milestone

The app currently runs fully offline and teaches the first production-oriented pieces:

- Kotlin data model for zodiac signs.
- Repository interface with an offline implementation.
- Jetpack Compose home screen with selectable sign cards.
- Navigation Compose route from the zodiac list to a detail screen.
- Detail UI state for loading, content, and error cases.
- Detail ViewModel that exposes screen state with `StateFlow`.
- Retrofit remote data source for daily horoscope text.
- Retry action when the daily horoscope network call fails.
- Reusable Compose components for list and detail UI.
- Basic unit tests for the horoscope data.

The reference repository at <https://github.com/IgniteCoders/Horoscopo-Android> was used only to compare the first feature goal: preparing the twelve zodiac signs with names, dates, and sign identity. This implementation uses its own Compose structure and offline content.

## Project structure

```text
app/src/main/java/com/example/horoscopo/
├── MainActivity.kt
├── data/
│   ├── HoroscopeDataSource.kt
│   ├── HoroscopeRepository.kt
│   ├── ZodiacSign.kt
│   └── remote/
│       ├── DailyHoroscopeApi.kt
│       ├── DailyHoroscopeResponse.kt
│       └── RemoteHoroscopeRepository.kt
├── navigation/
│   └── HoroscopeDestinations.kt
└── ui/
    ├── components/
    │   ├── HoroscopeDetailCard.kt
    │   └── ZodiacSignCard.kt
    ├── screens/
    │   └── HoroscopeApp.kt
    ├── state/
    │   └── HoroscopeDetailUiState.kt
    ├── viewmodel/
    │   └── HoroscopeDetailViewModel.kt
    └── theme/
        ├── Theme.kt
        └── Type.kt
```

## Run in Android Studio

1. Open this folder in Android Studio:
   `C:\Users\Cash\AndroidStudioProjects\Horoscopo_Android`
2. Let Gradle sync.
3. Choose an emulator or physical device.
4. Run the `app` configuration.

## Learning exercise

Before adding internet data, try this manually:

1. Add a new field to `ZodiacSign`, such as `bestMatch`.
2. Fill it in for all twelve signs in `HoroscopeRepository`.
3. Display it in `HoroscopeDetailCard`.
4. Add one unit test that checks Aries has the expected match.
5. Run the app and confirm the value appears only after opening a detail screen.

This practices the full path from model to data to UI to test.

## Why this interface matters

`HoroscopeDataSource` describes what the UI needs: a list of signs and a way to find one sign by ID. `HoroscopeRepository` currently returns offline data, but a future API class can implement the same interface. The Compose screens will not need to know where the data came from.

## Why UI state matters

`HoroscopeDetailUiState` describes what the detail screen can show: loading, content, or an error. The app still uses instant offline data, but this structure is ready for a network request where the screen may need to wait or recover from a missing result.

## Why ViewModel matters

`HoroscopeDetailViewModel` owns the detail screen state and exposes it as `StateFlow`. The composable collects that flow with lifecycle awareness, receives state, and renders it. This keeps UI code simpler as the app grows and prepares the detail screen for asynchronous work.

## Remote data

`RemoteHoroscopeRepository` uses Retrofit to request the daily horoscope from:

`https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily`

The app keeps zodiac metadata locally and replaces only the daily reading with the remote text. If the network request fails, the detail screen shows an error state with a retry action.

## Next recommended step

Add Compose UI tests for the home and detail screens so the visible behavior is covered, not only the Kotlin logic underneath it.
