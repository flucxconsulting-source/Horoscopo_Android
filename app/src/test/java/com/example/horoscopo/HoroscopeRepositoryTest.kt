package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.remote.RemoteHoroscopeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

class HoroscopeRepositoryTest {
    @Test
    fun repository_containsAllZodiacSigns() {
        val signs = HoroscopeRepository.getSigns()

        assertEquals(12, signs.size)
        assertEquals(12, signs.map { it.id }.distinct().size)
    }

    @Test
    fun repository_findsSignById() = runTest {
        val sign = HoroscopeRepository.getSign("aries")

        assertNotNull(sign)
        assertEquals("Aries", sign?.name)
    }

    @Test
    fun repository_canBeUsedThroughDataSourceInterface() = runTest {
        val dataSource: HoroscopeDataSource = HoroscopeRepository

        assertEquals("Leo", dataSource.getSign("leo")?.name)
    }

    @Test
    fun remoteRepository_replacesDailyReadingWhenApiReturnsText() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDivineHoroscopeApi(
                dailyHoroscope = "Remote horoscope text.",
            ),
            apiKey = "api-key",
            authToken = "auth-token",
        )

        val sign = repository.getSign("aries")

        assertEquals("Remote horoscope text.", sign?.dailyReading)
    }

    @Test
    fun remoteRepository_replacesWeekAndMonthReadingsWhenApiReturnsText() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDivineHoroscopeApi(
                dailyHoroscope = "Remote daily text.",
                weeklyHoroscope = "Remote weekly text.",
                monthlyHoroscope = "Remote monthly text.",
            ),
            apiKey = "api-key",
            authToken = "auth-token",
        )

        val sign = repository.getSign("aries")

        assertEquals("Remote weekly text.", sign?.weeklyReading)
        assertEquals("Remote monthly text.", sign?.monthlyReading)
    }

    @Test
    fun remoteRepository_usesFallbackWhenCredentialsAreMissing() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDivineHoroscopeApi(
                dailyHoroscope = "Remote horoscope text.",
            ),
            apiKey = "",
            authToken = "",
        )

        val sign = repository.getSign("aries")

        assertEquals(HoroscopeRepository.getSign("aries")?.dailyReading, sign?.dailyReading)
    }

    @Test
    fun remoteRepository_throwsWhenApiFails() {
        val repository = RemoteHoroscopeRepository(
            api = FakeDivineHoroscopeApi(
                throwable = IllegalStateException("Network failed"),
            ),
            apiKey = "api-key",
            authToken = "auth-token",
        )

        assertThrows(IllegalStateException::class.java) {
            runTest {
                repository.getSign("aries")
            }
        }
    }
}
