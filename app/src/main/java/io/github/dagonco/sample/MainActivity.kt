package io.github.dagonco.sample

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.dagonco.gsd.GoogleSupportedDevices
import io.github.dagonco.gsd.model.Device
import io.github.dagonco.sample.ui.theme.GoogleSupportedDevicesTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val deviceInfoViewModel: DeviceInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleSupportedDevicesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    DeviceInfoScreen(deviceInfoViewModel)
                }
            }
        }
    }
}

@Composable
fun DeviceInfoScreen(viewModel: DeviceInfoViewModel) {
    val device by viewModel.device.collectAsState()

    device?.let {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Manufacturer: ${it.manufacturer}")
            Text(text = "MarketName: ${it.marketName}")
            Text(text = "Codename: ${it.codename}")
            Text(text = "Model: ${it.model}")
        }
    }
}

class DeviceInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val gsd = GoogleSupportedDevices(application)

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device

    init {
        viewModelScope.launch {
            _device.value = gsd.getDevice()
        }
    }
}
