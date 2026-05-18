package io.github.panekd.stroopswap.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun GameScreen(toHome: () -> Unit) {
    val viewModel : GameViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // Pause when exit app
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.pause()
            }
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)

        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }

    Surface {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            if (state.paused) {
                PauseMenu(state.score, { viewModel.resume() }, toHome)
            } else {
                when (state.screen) {
                    GameScreenState.Question -> Question(
                        state, { viewModel.pause() },
                        { colour: Colours -> viewModel.onColourSelect(colour) })

                    GameScreenState.ModeChange -> ModeChange(
                        state.mode
                    ) { viewModel.startQuestion() }

                    GameScreenState.GameOver -> GameOver(state.score, toHome)
                }
            }
        }
    }
}

@Composable
fun ColourButtons(modifier: Modifier = Modifier, onClick: (Colours) -> Unit) {
    val settingsViewModel : SettingsViewModel = viewModel()
    val settings by settingsViewModel.settings.observeAsState()

    if (settings == null) return

    FlowRow (
        modifier = modifier.padding(4.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        Colours.entries.forEach { colour ->
            val modifier = Modifier
                .weight(1f)
                .then(
                    if (settings!!.doubleTap) {
                        Modifier.combinedClickable(
                            onClick = {},
                            onDoubleClick = { onClick(colour) }
                        )
                    } else {
                        Modifier.clickable { onClick(colour) }
                    }
                )

            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = colour.name,
                        color = colour.color,
                        fontSize = 25.sp
                    )
                }
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

@Composable
fun PauseMenu(score: Int, resume: () -> Unit, toHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Game paused")
        Text("Current score: $score")
        Button(onClick = resume) {
            Text("Resume")
        }
        Button(onClick = toHome) {
            Text("Quit")
        }
    }
}

@Composable
fun ModeChange(newMode: Modes, onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onContinue() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("New mode:")
            Text(newMode.toString())
        }
    }
}

@Composable
fun GameOver(score: Int, toHome: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game over")
        Text("Your score: $score")
        Button(toHome) {
            Text("Return to Menu")
        }
        Button({shareScore(context, score)}) {
            Text("Share score")
        }
    }
}

@Composable
fun Question(state: GameState, pause: () -> Unit, onColourSelect: (Colours) -> Unit) {
    val orientation = LocalConfiguration.current.orientation

    @Composable
    fun QuestionBox(modifier: Modifier = Modifier) {
        Box(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(pause) {
                    Icon(
                        painter = painterResource(R.drawable.pause_24px),
                        contentDescription = "Pause button"
                    )
                }
                Text("Score: " + state.score)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.currentWord.name,
                    color = state.currentColour.color,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold
                )
                Mode(state.mode)
            }
        }
    }

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row {
            QuestionBox(modifier = Modifier.weight(1f))
            ColourButtons(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    onClick = { colour: Colours -> onColourSelect(colour) }
                )
        }
    } else {
        Column {
            QuestionBox(modifier = Modifier.weight(1f))
            ColourButtons(
                modifier = Modifier
                    .weight(1f),
                onClick = { colour: Colours -> onColourSelect(colour) }
            )
        }
    }
}
