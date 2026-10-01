package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.navigation.HoroscopeDestinations
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun repository_containsAllZodiacSigns() {
        val signs = HoroscopeRepository.getSigns()

        assertEquals(12, signs.size)
        assertEquals(12, signs.map { it.id }.distinct().size)
    }

    @Test
    fun repository_findsSignById() {
        val sign = HoroscopeRepository.getSign("aries")

        assertNotNull(sign)
        assertEquals("Aries", sign?.name)
    }

    @Test
    fun repository_canBeUsedThroughDataSourceInterface() {
        val dataSource: HoroscopeDataSource = HoroscopeRepository

        assertEquals("Leo", dataSource.getSign("leo")?.name)
    }

    @Test
    fun detailRoute_includesSelectedSignId() {
        val route = HoroscopeDestinations.detailRoute("aries")

        assertEquals("detail/aries", route)
    }
}
