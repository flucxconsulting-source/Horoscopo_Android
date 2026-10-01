# Horoscope Android

Kotlin and Jetpack Compose training project for building a horoscope app step by step.

## Current milestone

The app currently runs fully offline and teaches the first production-oriented pieces:

- Kotlin data model for zodiac signs.
- Repository-style data source.
- Jetpack Compose screen with selectable sign cards.
- Reusable Compose components for list and detail UI.
- Basic unit tests for the horoscope data.

The reference repository at <https://github.com/IgniteCoders/Horoscopo-Android> was used only to compare the first feature goal: preparing the twelve zodiac signs with names, dates, and sign identity. This implementation uses its own Compose structure and offline content.

## Project structure

```text
app/src/main/java/com/example/horoscopo/
├── MainActivity.kt
├── data/
│   ├── HoroscopeRepository.kt
│   └── ZodiacSign.kt
└── ui/
    ├── components/
    │   ├── HoroscopeDetailCard.kt
    │   └── ZodiacSignCard.kt
    ├── screens/
    │   └── HoroscopeApp.kt
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

This practices the full path from model to data to UI to test.

## Next recommended step

Add a detail screen with Navigation Compose, then replace the offline daily reading with a small repository interface. After that, an API implementation can be added without rewriting the UI.
