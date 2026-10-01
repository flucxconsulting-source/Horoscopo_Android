package com.example.horoscopo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.state.buildHoroscopeDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HoroscopeDetailViewModel(
    signId: String?,
    repository: HoroscopeDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HoroscopeDetailUiState>(HoroscopeDetailUiState.Loading)
    val uiState: StateFlow<HoroscopeDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = buildHoroscopeDetailUiState(signId, repository)
        }
    }

    class Factory(
        private val signId: String?,
        private val repository: HoroscopeDataSource,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras,
        ): T {
            if (modelClass.isAssignableFrom(HoroscopeDetailViewModel::class.java)) {
                return HoroscopeDetailViewModel(signId, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
