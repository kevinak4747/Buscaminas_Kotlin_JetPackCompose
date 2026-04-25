package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

// Estado único del juego, lo que se dibuja en pantalla sale de aquí
// Este estado es inmutable, cada cambio crea una copia nueva y la UI se actualiza
// Esto es lo que hace que la UI se actualice sola al cambiar el estado.
data class GameUiState(

    // Número de filas del tablero
    val rows: Int = 8,

    // Número de columnas del tablero
    val cols: Int = 8,

    // Número total de minas en el tablero
    val mineCount: Int = 6,

    // Estado actual de la partida
    val status: GameStatus = GameStatus.PLAYING,

    // Tiempo transcurrido en segundos
    val elapsedSeconds: Int = 0,

    // Indica si se debe mostrar el botón de pausar o reanudar
    val showPauseOverlay: Boolean = false,

    // Tablero del juego
    // Lista de filas y cada fila contiene casillas
    val board: List<List<CellUi>> = emptyList(),

    // Guarda el efecto de sonido que la UI debe reproducir en ese momento
    val pendingSoundEffect: GameSoundEffect? = null
)


// Representa el estado general del juego
enum class GameStatus {
    PLAYING,
    PAUSED,
    WON,
    LOST
}


// Representa una casilla individual del tablero
data class CellUi(

    // Posición de la casilla dentro del tablero
    val row: Int,
    val col: Int,

    // Indica si esta casilla tiene una mina
    val isMine: Boolean,

    // Número de minas alrededor
    // Si es 0 es una casilla vacía
    val adjacentMines: Int,

    // Indica si la casilla ya está revelada
    val isRevealed: Boolean,

    // Indica si el usuario ha puesto una bandera
    val hasFlag: Boolean = false
)