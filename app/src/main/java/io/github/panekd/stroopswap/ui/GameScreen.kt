package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import io.github.panekd.stroopswap.R
import io.github.panekd.stroopswap.ui.theme.Red

enum class Modes {
    Colour,
    Word
}

@Composable
fun GameScreen() {
    var score by remember { mutableIntStateOf(0) }
    var mode by remember { mutableStateOf(Modes.Colour) }

    Column {
        IconButton({ pause() }) {
            Icon(
                painter = painterResource(R.drawable.pause_24px),
                contentDescription = "Pause button"
            )
        }
        Text("Score: $score")
        Text(
            text = "BLUE",
            color = Red,
            fontSize = 100.sp
        )
        Mode(mode)
    }
}

@Composable
fun Mode(mode: Modes) {
    val text = when (mode) {
        Modes.Colour -> "COLOUR MODE"
        Modes.Word -> "WORD MODE"
    }
    Text(
        text = text,
        fontSize = 20.sp
    )
}

fun pause() {

}