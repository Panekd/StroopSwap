package io.github.panekd.stroopswap.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun GameScreen() {
    var score by remember { mutableIntStateOf(0) }

    Text(score.toString())

}