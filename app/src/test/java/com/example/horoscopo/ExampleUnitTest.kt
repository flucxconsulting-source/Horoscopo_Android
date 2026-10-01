package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.remote.DailyHoroscopeApi
import com.example.horoscopo.data.remote.DailyHoroscopeDto
import com.example.horoscopo.data.remote.DailyHoroscopeResponse
import com.example.horoscopo.data.remote.RemoteHoroscopeRepository
import com.example.horoscopo.navigation.HoroscopeDestinations
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.state.buildHoroscopeDetailUiState
import com.example.horoscopo.ui.viewmodel.HoroscopeDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
    fun detailRoute_includesSelectedSignId() {
        val route = HoroscopeDestinations.detailRoute("aries")

        assertEquals("detail/aries", route)
    }

    @Test
    fun detailUiState_returnsContentForKnownSign() = runTest {
        val uiState = buildHoroscopeDetailUiState("aries", HoroscopeRepository)

        assertTrue(uiState is HoroscopeDetailUiState.Content)
        assertEquals("Aries", (uiState as HoroscopeDetailUiState.Content).sign.name)
    }

    @Test
    fun detailUiState_returnsErrorForUnknownSign() = runTest {
        val uiState = buildHoroscopeDetailUiState("ophiuchus", HoroscopeRepository)

        assertTrue(uiState is HoroscopeDetailUiState.Error)
    }

    @Test
    fun detailViewModel_exposesContentStateForKnownSign() = runTest {
        val viewModel = HoroscopeDetailViewModel("aries", HoroscopeRepository)

        assertTrue(viewModel.uiState.value is HoroscopeDetailUiState.Content)
    }

    @Test
    fun remoteRepository_replacesDailyReadingWhenApiReturnsText() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDailyHoroscopeApi(
                horoscope = "Remote horoscope text.",
            ),
        )

        val sign = repository.getSign("aries")

        assertEquals("Remote horoscope text.", sign?.dailyReading)
    }

    @Test
    fun remoteRepository_usesFallbackWhenApiFails() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDailyHoroscopeApi(
                throwable = IllegalStateException("Network failed"),
            ),
        )

        val sign = repository.getSign("aries")

        assertEquals(HoroscopeRepository.getSign("aries")?.dailyReading, sign?.dailyReading)
    }

    private class FakeDailyHoroscopeApi(
        private val horoscope: String? = null,
        private val throwable: Throwable? = null,
    ) : DailyHoroscopeApi {
        override suspend fun getDailyHoroscope(
            sign: String,
            day: String,
        ): DailyHoroscopeResponse {
            throwable?.let { throw it }
            return DailyHoroscopeResponse(
                data = DailyHoroscopeDto(
                    date = "2026-10-01",
                    period = "daily",
                    sign = sign,
                    horoscope = horoscope,
                ),
            )
        }
    }
}
