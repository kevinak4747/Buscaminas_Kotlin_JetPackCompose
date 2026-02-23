package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

// Eventos que la UI le envía al ViewModel.
// La UI no decide nada: solo avisa de lo que el usuario hizo.
sealed interface GameEvent {
    data class CellPressed(val row: Int, val col: Int) : GameEvent
    data object RestartPressed : GameEvent
}