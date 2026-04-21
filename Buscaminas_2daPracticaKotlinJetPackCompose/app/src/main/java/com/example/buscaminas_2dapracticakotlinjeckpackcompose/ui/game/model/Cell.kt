package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game.model

// Esta clase representa una casilla del tablero del Buscaminas
// Cada celda sabe si tiene mina, si está revelada, si tiene bandera
// y cuántas minas tiene alrededor
data class Cell(

    // Indica si esta casilla contiene una mina
    val hasMine: Boolean = false,

    // Indica si el usuario ya ha revelado esta casilla
    val isRevealed: Boolean = false,

    // Indica si el usuario ha puesto una bandera en esta casilla
    val hasFlag: Boolean = false,

    // Número de minas en las casillas vecinas (0 = casilla vacía)
    val adjacentMines: Int = 0
)