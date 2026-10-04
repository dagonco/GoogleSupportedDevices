package io.github.dagonco.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.github.dagonco.sample.ui.theme.GoogleSupportedDevicesTheme

class MainActivity : ComponentActivity() {

    private val deviceInfoViewModel: DeviceInfoViewModel by viewModels { DeviceInfoViewModel.factory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GoogleSupportedDevicesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DeviceInfoScreen(
                        viewModel = deviceInfoViewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
