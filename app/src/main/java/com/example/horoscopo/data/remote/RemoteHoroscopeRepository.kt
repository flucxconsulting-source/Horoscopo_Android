package com.example.horoscopo.data.remote

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteHoroscopeRepository(
    private val api: DailyHoroscopeApi,
    private val fallback: HoroscopeDataSource = HoroscopeRepository,
) : HoroscopeDataSource {
    override fun getSigns(): List<ZodiacSign> = fallback.getSigns()

    override suspend fun getSign(id: String): ZodiacSign? {
        val localSign = fallback.getSign(id) ?: return null

        val response = api.getDailyHoroscope(sign = localSign.name)
        val remoteReading = response.data?.horoscope?.takeIf { it.isNotBlank() }
        return if (remoteReading == null) {
            localSign
        } else {
            localSign.copy(dailyReading = remoteReading)
        }
    }

    companion object {
        private const val BASE_URL = "https://horoscope-app-api.vercel.app/"

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
                api = retrofit.create(DailyHoroscopeApi::class.java),
                fallback = fallback,
            )
        }
    }
}
