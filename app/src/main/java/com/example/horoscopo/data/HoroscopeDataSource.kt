package com.example.horoscopo.data

interface HoroscopeDataSource {
    fun getSigns(): List<ZodiacSign>

    suspend fun getSign(id: String): ZodiacSign?
}
