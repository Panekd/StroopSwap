package io.github.panekd.stroopswap.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.util.Locale

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
fun GameScreen(toHome: () -> Unit, scoreVM: ScoreViewModel) {
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

    fun onQuit() {
        scoreVM.add(state.score)
        toHome()
    }

    Surface {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            if (state.paused) {
                PauseMenu(state.score, { viewModel.resume() }, { onQuit() })
            } else {
                when (state.screen) {
                    GameScreenState.Question -> Question(
                        state, { viewModel.pause() },
                        { colour: Colours -> viewModel.onColourSelect(colour) })

                    GameScreenState.ModeChange -> ModeChange(
                        state.mode
                    ) { viewModel.startQuestion() }

                    GameScreenState.GameOver ->
                        GameOver(state.score,
                            { viewModel.restart() },
                            { onQuit() })
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
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 2
    ) {
        Colours.entries.forEach { colour ->

            Surface(
                modifier = Modifier
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
                    ),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = colour.name,
                        modifier = Modifier.align(Alignment.Center),
                        color = colour.color,
                        fontSize = 25.sp,
                        softWrap = false,
                        maxLines = 1
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
        Modes.Colour -> stringResource(R.string.colour_mode)
        Modes.Word -> stringResource(R.string.word_mode)
    }

    Text(
        text = text,
        fontSize = 30.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun PauseMenu(score: Int, resume: () -> Unit, onQuit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(stringResource(R.string.paused), 
            fontSize = 60.sp,
            lineHeight = 65.sp,
            textAlign = TextAlign.Center)
        Text(String.format(
                Locale.UK,
                stringResource(R.string.show_score),
                score), 
            fontSize = 20.sp,
            textAlign = TextAlign.Center)
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Button(onClick = resume, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.resume), fontSize = 32.sp)
            }
            Button(onClick = onQuit, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.quit), fontSize = 32.sp)
            }
        }
    }
}

@Composable
fun ModeChange(newMode: Modes, onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onContinue() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.new_mode), 
                fontSize = 60.sp,
                lineHeight = 65.sp,
                textAlign = TextAlign.Center)
            Text(newMode.toString(), 
                fontSize = 60.sp,
                lineHeight = 65.sp,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun GameOver(score: Int, tryAgain: () -> Unit, onQuit: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.game_over), 
            fontSize = 60.sp,
            lineHeight = 65.sp,
            textAlign = TextAlign.Center)
        Text(String.format(Locale.UK,
                stringResource(R.string.final_score),
                score), 
            fontSize = 20.sp,
            textAlign = TextAlign.Center)
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Button(
                tryAgain,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.try_again), 
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center)
            }
            Button(
                onQuit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.to_menu), 
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center)
            }
            Button(
                { shareScore(context, score) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.share_score), 
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center)
            }
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
                        contentDescription = stringResource(R.string.pause)
                    )
                }
                Text(String.format(
                        Locale.UK,
                        stringResource(R.string.show_score),
                        state.score),
                    fontSize = 20.sp)
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
                    fontWeight = FontWeight.Bold,
                    softWrap = false,
                    maxLines = 1
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
                    onClick = onColourSelect
                )
        }
    } else {
        Column {
            QuestionBox(modifier = Modifier.weight(1f))
            ColourButtons(
                modifier = Modifier
                    .weight(1.5f),
                onClick = onColourSelect
            )
        }
    }
}
