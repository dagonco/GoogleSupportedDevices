package io.github.dagonco.sample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import io.github.dagonco.gsd.GoogleSupportedDevices
import io.github.dagonco.gsd.model.Device
import io.github.dagonco.sample.ui.theme.GoogleSupportedDevicesTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    private val deviceInfoViewModel = DeviceInfoViewModel(this)

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
fun DeviceInfoScreen(
    viewModel: DeviceInfoViewModel,
) {
    val device by viewModel.device.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getDevice()
    }

    device?.let {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Manufacturer: ${device?.manufacturer}")
            Text(text = "MarketName: ${device?.marketName}")
            Text(text = "Codename: ${device?.codename}")
            Text(text = "Model: ${device?.model}")
        }
    }
}

class DeviceInfoViewModel(context: Context) : ViewModel() {
    private val gsd = GoogleSupportedDevices(context)

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device

    suspend fun getDevice() {
        _device.value = gsd.getDevice()
    }
}