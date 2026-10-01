package com.example.horoscopo

import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.remote.RemoteHoroscopeRepository
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.viewmodel.HoroscopeDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HoroscopeDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun detailViewModel_exposesContentStateForKnownSign() = runTest {
        val viewModel = HoroscopeDetailViewModel("aries", HoroscopeRepository)

        assertTrue(viewModel.uiState.value is HoroscopeDetailUiState.Content)
    }

    @Test
    fun detailViewModel_canRetryAfterNetworkFailure() = runTest {
        val repository = RemoteHoroscopeRepository(
            api = FakeDailyHoroscopeApi(
                horoscopes = mutableListOf(null, "Recovered horoscope text."),
                throwable = IllegalStateException("Network failed"),
            ),
        )
        val viewModel = HoroscopeDetailViewModel("aries", repository)

        assertTrue(viewModel.uiState.value is HoroscopeDetailUiState.Error)

        viewModel.retry()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is HoroscopeDetailUiState.Content)
        assertEquals(
            "Recovered horoscope text.",
            (uiState as HoroscopeDetailUiState.Content).sign.dailyReading,
        )
    }
}
