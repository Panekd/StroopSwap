package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.panekd.stroopswap.R

@Composable
fun HomeScreen(toSettings: () -> Unit, toInfo: () -> Unit, toGame: () -> Unit) {
    Column {
        Row {
            Spacer(Modifier.weight(1f))
            Button(onClick = { toSettings() }) { Text("Settings") }
            Button(onClick = { toInfo() }) { Text("Info") }
        }
        Text(stringResource(R.string.app_name))
        Button(onClick = { toGame() }) { Text("Play") }
        Text("High score: XXX")
    }
}
