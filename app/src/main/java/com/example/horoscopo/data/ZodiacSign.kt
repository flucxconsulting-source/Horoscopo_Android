package com.example.horoscopo.data

data class ZodiacSign(
    val id: String,
    val name: String,
    val dateRange: String,
    val symbol: String,
    val element: String,
    val rulingPlanet: String,
    val luckyColor: String,
    val luckyNumber: Int,
    val summary: String,
    val dailyReading: String,
    val weeklyReading: String? = null,
    val monthlyReading: String? = null,
)
