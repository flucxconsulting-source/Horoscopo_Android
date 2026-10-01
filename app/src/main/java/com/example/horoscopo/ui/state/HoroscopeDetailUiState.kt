package com.example.horoscopo.ui.state

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.ZodiacSign

sealed interface HoroscopeDetailUiState {
    data object Loading : HoroscopeDetailUiState

    data class Content(val sign: ZodiacSign) : HoroscopeDetailUiState

    data class Error(val message: String) : HoroscopeDetailUiState
}

suspend fun buildHoroscopeDetailUiState(
    signId: String?,
    repository: HoroscopeDataSource,
): HoroscopeDetailUiState {
    if (signId.isNullOrBlank()) {
        return HoroscopeDetailUiState.Error("Missing zodiac sign.")
    }

    return runCatching {
        val sign = repository.getSign(signId)
        if (sign == null) {
            HoroscopeDetailUiState.Error("This zodiac sign is not available.")
        } else {
            HoroscopeDetailUiState.Content(sign)
        }
    }.getOrElse {
        HoroscopeDetailUiState.Error("Could not load today's horoscope. Check your connection and try again.")
    }
}
