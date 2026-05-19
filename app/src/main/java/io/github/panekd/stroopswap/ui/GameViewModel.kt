package io.github.panekd.stroopswap.ui

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel() : ViewModel() {
    private val _state = MutableStateFlow(
        GameState(
            mode = Modes.entries.random(),
            currentWord = Colours.entries.random(),
            currentColour = Colours.entries.random()
        )
    )
    val state: StateFlow<GameState> = _state

    private var questionStartTime = 0L
    private var pauseStartTime = 0L
    private var pausedDuration = 0L

    fun pause() {
        if (_state.value.paused) return

        pauseStartTime = SystemClock.elapsedRealtime()
        _state.update {
            it.copy(paused = true)
        }
    }

    fun resume() {
        if (!_state.value.paused) return

        pausedDuration += SystemClock.elapsedRealtime() - pauseStartTime
        _state.update {
            it.copy(paused = false)
        }
    }

    private fun currentQuestionTime(): Long {
        return SystemClock.elapsedRealtime() -
                questionStartTime -
                pausedDuration
    }

    internal fun startQuestion() {
        questionStartTime = SystemClock.elapsedRealtime()
        pausedDuration = 0L

        _state.update {
            it.copy(
                screen = GameScreenState.Question,
                currentWord = Colours.entries.random(),
                currentColour = Colours.entries.random()
            )
        }
    }

    fun onColourSelect(colour: Colours) {
        val current = _state.value

        if (colour == correctColour(current.mode, current.currentWord, current.currentColour)) {
            val nextMode =
                if (Random.nextFloat() < 0.2f)
                    Modes.entries.random()
                else
                    current.mode

            _state.update {
                it.copy(
                    score = it.score + (2000 / currentQuestionTime()).toInt(),
                    mode = nextMode,
                    screen =
                        if (nextMode != current.mode)
                            GameScreenState.ModeChange
                        else
                            GameScreenState.Question
                )
            }

            if (nextMode == current.mode) {
                startQuestion()
            }
        } else {
            _state.update {
                it.copy(screen = GameScreenState.GameOver)
            }
        }
    }
}