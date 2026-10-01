package com.example.horoscopo.ui.state

import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.ZodiacSign

sealed interface HoroscopeDetailUiState {
    data object Loading : HoroscopeDetailUiState

    data class Content(val sign: ZodiacSign) : HoroscopeDetailUiState

    data class Error(val message: String) : HoroscopeDetailUiState
}

fun buildHoroscopeDetailUiState(
    signId: String?,
    repository: HoroscopeDataSource,
): HoroscopeDetailUiState {
    if (signId.isNullOrBlank()) {
        return HoroscopeDetailUiState.Error("Missing zodiac sign.")
    }

    val sign = repository.getSign(signId)
    return if (sign == null) {
        HoroscopeDetailUiState.Error("This zodiac sign is not available.")
    } else {
        HoroscopeDetailUiState.Content(sign)
    }
}
