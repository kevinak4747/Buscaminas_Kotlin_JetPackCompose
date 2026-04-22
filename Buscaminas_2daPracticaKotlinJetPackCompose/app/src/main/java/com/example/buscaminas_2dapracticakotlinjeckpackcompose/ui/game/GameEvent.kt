package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

// Eventos que la UI le envía al ViewModel pero antes pasa por aquí para que el ViewModel
// no tenga que saber nada de la UI.
// La UI no decide nada, solo avisa de lo que hace el usuario
sealed interface GameEvent {

    // El usuario pulsa una casilla para abrirla
    data class CellPressed(
        val row: Int,
        val col: Int
    ) : GameEvent

    // El usuario hace pulsación larga para poner o quitar bandera
    data class CellLongPressed(
        val row: Int,
        val col: Int
    ) : GameEvent

    // El usuario pulsa el botón de reiniciar partida
    data object RestartPressed : GameEvent

    // La app pierde el foco y la partida debe pausarse
    data object AppPaused : GameEvent

    // El usuario pulsa el botón de pausa manual
    data object PausePressed : GameEvent

    // El usuario pulsa reanudar después de volver a la app
    data object ResumePressed : GameEvent
}