package com.example.horoscopo

import com.example.horoscopo.data.remote.DivineHoroscopeApi
import com.example.horoscopo.data.remote.DivineHoroscopeData
import com.example.horoscopo.data.remote.DivineHoroscopeResponse

class FakeDivineHoroscopeApi(
    private val dailyHoroscope: String? = null,
    private val weeklyHoroscope: String? = null,
    private val monthlyHoroscope: String? = null,
    private val dailyHoroscopes: MutableList<String?> = mutableListOf(),
    private val throwable: Throwable? = null,
) : DivineHoroscopeApi {
    override suspend fun getDailyHoroscope(
        authorization: String,
        apiKey: String,
        sign: String,
        day: String,
        timeZone: String,
        language: String,
    ): DivineHoroscopeResponse {
        if (dailyHoroscopes.isNotEmpty()) {
            val nextHoroscope = dailyHoroscopes.removeAt(0)
            if (nextHoroscope == null) {
                throwable?.let { throw it }
            }
            return responseFor(sign, nextHoroscope)
        }

        throwable?.let { throw it }
        return responseFor(sign, dailyHoroscope)
    }

    override suspend fun getWeeklyHoroscope(
        authorization: String,
        apiKey: String,
        sign: String,
        week: String,
        timeZone: String,
        language: String,
    ): DivineHoroscopeResponse = responseFor(sign, weeklyHoroscope)

    override suspend fun getMonthlyHoroscope(
        authorization: String,
        apiKey: String,
        sign: String,
        month: String,
        timeZone: String,
        language: String,
    ): DivineHoroscopeResponse = responseFor(sign, monthlyHoroscope)

    private fun responseFor(
        sign: String,
        horoscope: String?,
    ): DivineHoroscopeResponse = DivineHoroscopeResponse(
        data = DivineHoroscopeData(
            date = "2026-10-02",
            period = "current",
            sign = sign,
            horoscope = horoscope,
        ),
    )
}
