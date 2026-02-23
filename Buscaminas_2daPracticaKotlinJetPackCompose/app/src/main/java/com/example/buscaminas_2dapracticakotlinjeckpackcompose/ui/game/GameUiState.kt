package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

// Estado único del juego.
// Todo lo que se dibuja en pantalla sale de aquí.
data class GameUiState(
    val rows: Int = 3,
    val cols: Int = 3,
    val status: GameStatus = GameStatus.PLAYING,
    val elapsedSeconds: Int = 0,
    val board: List<List<CellUi>> = emptyList()
)

// Estado general de la partida.
enum class GameStatus {
    PLAYING,
    WON,
    LOST
}

// Estado de cada casilla.
data class CellUi(
    val row: Int,
    val col: Int,
    val isMine: Boolean,
    val adjacentMines: Int,
    val isRevealed: Boolean
)