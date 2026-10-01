package com.example.horoscopo.data.remote

data class DailyHoroscopeResponse(
    val data: DailyHoroscopeDto?,
)

data class DailyHoroscopeDto(
    val date: String?,
    val period: String?,
    val sign: String?,
    val horoscope: String?,
)
