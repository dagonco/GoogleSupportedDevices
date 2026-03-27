package io.github.dagonco.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import io.github.dagonco.sample.ui.theme.GoogleSupportedDevicesTheme

class MainActivity : ComponentActivity() {

    private val deviceInfoViewModel: DeviceInfoViewModel by viewModels { DeviceInfoViewModel.factory(this) }

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
