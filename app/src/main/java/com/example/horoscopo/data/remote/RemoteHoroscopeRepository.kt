package com.example.horoscopo.data.remote

import com.example.horoscopo.BuildConfig
import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteHoroscopeRepository(
    private val api: DivineHoroscopeApi,
    private val apiKey: String,
    private val authToken: String,
    private val timeZone: String = "1",
    private val language: String = "en",
    private val fallback: HoroscopeDataSource = HoroscopeRepository,
) : HoroscopeDataSource {
    override fun getSigns(): List<ZodiacSign> = fallback.getSigns()

    override suspend fun getSign(id: String): ZodiacSign? {
        val localSign = fallback.getSign(id) ?: return null
        if (!hasCredentials()) return localSign

        val authorization = "Bearer $authToken"
        val dailyReading = api.getDailyHoroscope(
            authorization = authorization,
            apiKey = apiKey,
            sign = localSign.name,
            timeZone = timeZone,
            language = language,
        ).extractReading()
        val weeklyReading = api.getWeeklyHoroscope(
            authorization = authorization,
            apiKey = apiKey,
            sign = localSign.name,
            timeZone = timeZone,
            language = language,
        ).extractReading()
        val monthlyReading = api.getMonthlyHoroscope(
            authorization = authorization,
            apiKey = apiKey,
            sign = localSign.name,
            timeZone = timeZone,
            language = language,
        ).extractReading()

        return localSign.copy(
            dailyReading = dailyReading ?: localSign.dailyReading,
            weeklyReading = weeklyReading,
            monthlyReading = monthlyReading,
        )
    }

    private fun hasCredentials(): Boolean = apiKey.isNotBlank() && authToken.isNotBlank()

    private fun DivineHoroscopeResponse.extractReading(): String? {
        return listOfNotNull(
            data?.horoscope,
            data?.botResponse,
            data?.prediction?.readablePrediction(),
            prediction?.readablePrediction(),
            horoscope,
        ).firstOrNull { it.isNotBlank() }
    }

    private fun Map<String, String>.readablePrediction(): String? {
        return values
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(separator = "\n\n")
            .takeIf { it.isNotBlank() }
    }

    companion object {
        private const val BASE_URL = "https://astroapi-5.divineapi.com/"

        fun create(
            fallback: HoroscopeDataSource = HoroscopeRepository,
        ): RemoteHoroscopeRepository {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return RemoteHoroscopeRepository(
                api = retrofit.create(DivineHoroscopeApi::class.java),
                apiKey = BuildConfig.DIVINE_API_KEY,
                authToken = BuildConfig.DIVINE_AUTH_TOKEN,
                fallback = fallback,
            )
        }
    }
}
