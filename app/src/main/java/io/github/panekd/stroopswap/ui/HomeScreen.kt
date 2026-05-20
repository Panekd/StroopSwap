package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.panekd.stroopswap.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(toSettings: () -> Unit, toInfo: () -> Unit, toGame: () -> Unit, scoreVM: ScoreViewModel) {
    val highScore = scoreVM.highScore.observeAsState()

    Scaffold (
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = toSettings
                    ) {
                        Icon(Icons.Outlined.Settings,
                            stringResource(R.string.settings))
                    }
                    IconButton(
                        onClick = toInfo
                    ) {
                        Icon(Icons.Outlined.Info,
                            stringResource(R.string.information))
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.app_name),
                fontSize = 80.sp,
                lineHeight = 80.sp,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = toGame,
                modifier = Modifier.size(120.dp)) {
                Icon(Icons.Filled.PlayArrow,
                    "Play",
                    tint = Color(0xff00e000),
                    modifier = Modifier.size(100.dp))
            }
            Text(String.format(
                    Locale.UK,
                    stringResource(R.string.high_score),
                    highScore.value?.score?: 0),
                fontSize = 20.sp)
        }
    }
}
