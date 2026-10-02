package com.example.horoscopo.data

enum class HoroscopePeriod(
    val label: String,
    val title: String,
) {
    Day(label = "Day", title = "Today"),
    Week(label = "Week", title = "This week"),
    Month(label = "Month", title = "This month"),
}

fun ZodiacSign.readingFor(period: HoroscopePeriod): String = when (period) {
    HoroscopePeriod.Day -> dailyReading
    HoroscopePeriod.Week -> "This week favors your ${element.lowercase()} nature. ${summary}"
    HoroscopePeriod.Month -> "This month, work with ${rulingPlanet}'s influence and keep ${luckyColor.lowercase()} close as a reminder to move with intention."
}
