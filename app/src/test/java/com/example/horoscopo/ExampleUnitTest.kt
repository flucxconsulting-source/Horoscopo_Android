package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
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

    @Test
    fun detailUiState_returnsContentForKnownSign() {
        val uiState = buildHoroscopeDetailUiState("aries", HoroscopeRepository)

        assertTrue(uiState is HoroscopeDetailUiState.Content)
        assertEquals("Aries", (uiState as HoroscopeDetailUiState.Content).sign.name)
    }

    @Test
    fun detailUiState_returnsErrorForUnknownSign() {
        val uiState = buildHoroscopeDetailUiState("ophiuchus", HoroscopeRepository)

        assertTrue(uiState is HoroscopeDetailUiState.Error)
    }

    @Test
    fun detailViewModel_exposesContentStateForKnownSign() = runTest {
        val viewModel = HoroscopeDetailViewModel("aries", HoroscopeRepository)

        assertTrue(viewModel.uiState.value is HoroscopeDetailUiState.Content)
    }
}
