package io.github.dagonco.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.dagonco.gsd.GoogleSupportedDevices
import io.github.dagonco.gsd.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data class Ready(val device: Device) : UiState
}

class DeviceInfoViewModel(private val gsd: GoogleSupportedDevices) : ViewModel() {

    companion object {
        fun factory(context: Context) = viewModelFactory {
            initializer {
                DeviceInfoViewModel(GoogleSupportedDevices(context.applicationContext))
            }
        }
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = UiState.Ready(gsd.getDevice())
        }
    }
}
