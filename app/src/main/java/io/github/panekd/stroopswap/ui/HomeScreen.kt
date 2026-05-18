package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.github.panekd.stroopswap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(toSettings: () -> Unit, toInfo: () -> Unit, toGame: () -> Unit) {
    Scaffold (
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(
                        onClick = toSettings
                    ) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                    IconButton(
                        onClick = toInfo
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = "Information")
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(stringResource(R.string.app_name))
            Button(onClick = { toGame() }) { Text("Play") }
            Text("High score: XXX")
        }
    }
}
