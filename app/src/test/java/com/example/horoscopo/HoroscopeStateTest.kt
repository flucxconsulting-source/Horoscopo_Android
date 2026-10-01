package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.navigation.HoroscopeDestinations
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.state.buildHoroscopeDetailUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HoroscopeStateTest {
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
}
