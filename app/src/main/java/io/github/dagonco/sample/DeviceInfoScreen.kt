package io.github.dagonco.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.dagonco.sample.components.DeviceContent
import io.github.dagonco.sample.components.LoadingContent

@Composable
fun DeviceInfoScreen(viewModel: DeviceInfoViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val s = state) {
            is UiState.Loading -> LoadingContent()
            is UiState.Ready -> DeviceContent(s.device)
        }
    }
}
