package io.github.panekd.stroopswap.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit) {
    Text("Hi")
    Button( onClick = { onNavigateToSettings() } ){ Text("Settings") }
}
