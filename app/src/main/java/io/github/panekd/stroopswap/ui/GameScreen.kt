package io.github.panekd.stroopswap.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.panekd.stroopswap.R
import io.github.panekd.stroopswap.ui.theme.Blue
import io.github.panekd.stroopswap.ui.theme.Green
import io.github.panekd.stroopswap.ui.theme.Orange
import io.github.panekd.stroopswap.ui.theme.Purple
import io.github.panekd.stroopswap.ui.theme.Red
import io.github.panekd.stroopswap.ui.theme.Yellow

enum class Modes {
    Colour,
    Word
}

enum class Colours (val color: Color) {
    RED(Red),
    ORANGE(Orange),
    YELLOW(Yellow),
    GREEN(Green),
    BLUE(Blue),
    PURPLE(Purple)
}

@Composable
fun GameScreen(onCorrect: () -> Unit = {}, onFail: () -> Unit = {}) {
    val orientation = LocalConfiguration.current.orientation

    var score by remember { mutableIntStateOf(0) }
    var mode by remember { mutableStateOf(Modes.entries.random()) }
    var currentWord by remember { mutableStateOf(Colours.entries.random()) }
    var currentColour by remember { mutableStateOf(Colours.entries.random()) }

    Column {
        IconButton({ pause() }) {
            Icon(
                painter = painterResource(R.drawable.pause_24px),
                contentDescription = "Pause button"
            )
        }
        Text("Score: $score")
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Row {
                Column {
                    Text(
                        text = currentWord.name,
                        color = currentColour.color,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Mode(mode)
                }
                ColourButtons(onClick = { colour: Colours ->
                    {
                        if (colour == correctColour(mode, currentWord, currentColour)) {
                            onCorrect()
                            // change word + colour
                            // increase score
                        } else {
                            onFail()
                            // show end screen
                        }
                    }
                })
            }
        } else {
            Column{
                Text(
                    text = currentWord.name,
                    color = currentColour.color,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold
                )
                Mode(mode)
                ColourButtons(onClick = { colour: Colours ->
                    {
                        if (colour == correctColour(mode, currentWord, currentColour)) {
                            onCorrect()
                            // change word + colour
                            // increase score
                        } else {
                            onFail()
                            // show end screen
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun ColourButtons(onClick: (Colours) -> Unit) {
    FlowRow (
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 2
    ) {
        Colours.entries.forEach { colour ->
            OutlinedButton(onClick=({ onClick(colour) })) {
                Text(colour.name, color=colour.color, fontSize=25.sp)
            }
        }
    }
}

fun correctColour(mode: Modes, currentWord:Colours, currentColour: Colours): Colours {
    return when (mode) {
        Modes.Colour -> currentColour
        Modes.Word -> currentWord
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
        fontSize = 25.sp
    )
}

fun pause() {

}