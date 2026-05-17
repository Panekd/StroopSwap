package io.github.panekd.stroopswap.ui

data class GameState(
    val score: Int = 0,
    val mode: Modes,
    val currentWord: Colours,
    val currentColour: Colours,
    val screen: GameScreenState = GameScreenState.ModeChange,
    val paused: Boolean = false
)

enum class GameScreenState {
    ModeChange,
    Question,
    GameOver
}