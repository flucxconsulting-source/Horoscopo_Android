package com.example.horoscopo.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface DailyHoroscopeApi {
    @GET("api/v1/get-horoscope/daily")
    suspend fun getDailyHoroscope(
        @Query("sign") sign: String,
        @Query("day") day: String = "TODAY",
    ): DailyHoroscopeResponse
}
