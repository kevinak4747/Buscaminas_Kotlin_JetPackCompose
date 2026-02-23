package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.result

import androidx.compose.runtime.Composable

// ResultRoute conecta la navegación con la pantalla ResultScreen.
@Composable
fun ResultRoute(

    // Texto que indica si el jugador ganó o perdió.
    title: String,

    // Acción que se ejecuta al pulsar "Jugar otra".
    onPlayAgain: () -> Unit,

    // Acción que se ejecuta al pulsar "Volver al menú".
    onBackToMenu: () -> Unit
) {

    // Delego la UI en ResultScreen y solo paso los datos y acciones.
    ResultScreen(
        title = title,
        onPlayAgain = onPlayAgain,
        onBackToMenu = onBackToMenu
    )
}