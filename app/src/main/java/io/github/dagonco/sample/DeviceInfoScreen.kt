package io.github.dagonco.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.dagonco.sample.components.DeviceContent
import io.github.dagonco.sample.components.LoadingContent

@Composable
fun DeviceInfoScreen(viewModel: DeviceInfoViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        when (val s = state) {
            is UiState.Loading -> LoadingContent()
            is UiState.Ready -> DeviceContent(s.device)
        }
    }
}
