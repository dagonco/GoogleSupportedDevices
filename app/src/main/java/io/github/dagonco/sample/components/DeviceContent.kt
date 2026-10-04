package io.github.dagonco.sample.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.dagonco.gsd.model.Device
import io.github.dagonco.sample.ui.theme.GoogleSupportedDevicesTheme

@Composable
internal fun DeviceContent(device: Device) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = device.marketName,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = device.manufacturer,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "DETAILS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                HorizontalDivider()
                DetailRow(label = "Manufacturer", value = device.manufacturer)
                HorizontalDivider()
                DetailRow(label = "Market Name", value = device.marketName)
                HorizontalDivider()
                DetailRow(label = "Model", value = device.model)
                HorizontalDivider()
                DetailRow(label = "Codename", value = device.codename)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceContentPreview() {
    GoogleSupportedDevicesTheme {
        DeviceContent(Device("Samsung", "Galaxy S10", "beyond1", "SM-G973F"))
    }
}
