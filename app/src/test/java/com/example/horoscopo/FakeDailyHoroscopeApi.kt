package com.example.horoscopo

import com.example.horoscopo.data.remote.DailyHoroscopeApi
import com.example.horoscopo.data.remote.DailyHoroscopeDto
import com.example.horoscopo.data.remote.DailyHoroscopeResponse

class FakeDailyHoroscopeApi(
    private val horoscope: String? = null,
    private val horoscopes: MutableList<String?> = mutableListOf(),
    private val throwable: Throwable? = null,
) : DailyHoroscopeApi {
    override suspend fun getDailyHoroscope(
        sign: String,
        day: String,
    ): DailyHoroscopeResponse {
        if (horoscopes.isNotEmpty()) {
            val nextHoroscope = horoscopes.removeAt(0)
            if (nextHoroscope == null) {
                throwable?.let { throw it }
            }
            return responseFor(sign, nextHoroscope)
        }

        throwable?.let { throw it }
        return responseFor(sign, horoscope)
    }

    private fun responseFor(
        sign: String,
        horoscope: String?,
    ): DailyHoroscopeResponse = DailyHoroscopeResponse(
        data = DailyHoroscopeDto(
            date = "2026-10-01",
            period = "daily",
            sign = sign,
            horoscope = horoscope,
        ),
    )
}
