package com.example.horoscopo.data

interface HoroscopeDataSource {
    fun getSigns(): List<ZodiacSign>

    fun getSign(id: String): ZodiacSign?
}
