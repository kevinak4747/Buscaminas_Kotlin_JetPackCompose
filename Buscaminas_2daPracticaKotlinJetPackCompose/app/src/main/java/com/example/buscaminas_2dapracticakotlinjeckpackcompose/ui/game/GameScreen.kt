package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue  // Para usar el valor animado con by
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale

// GameScreen es una pantalla "tonta":
// solo dibuja el estado y envía eventos al ViewModel.
@Composable
fun GameScreen(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {

    // Decido el color objetivo según el estado del juego.
    val targetColor = when (uiState.status) {
        GameStatus.WON -> Color(0xFFB9F6CA)   // Verde suave
        GameStatus.LOST -> Color(0xFFFFCDD2)  // Rojo suave
        else -> Color.White                  // Estado normal
    }

    // Animo el fondo cuando cambia el estado (ganar o perder).
    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 800)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(animatedBackgroundColor),  // Aplico el color animado al fondo de la pantalla, en caso de ganar o perder se animará el fondo al color correspondiente.
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Título de pantalla.
        Text(
            text = "Buscaminas",
            style = MaterialTheme.typography.headlineMedium
        )

        // Mostramos el estado actual de la partida.
        Text(text = "Estado: ${uiState.status}")

        // Botón para reiniciar (manda evento al ViewModel).
        Button(onClick = { onEvent(GameEvent.RestartPressed) }) {
            Text(text = "Reiniciar")
        }

        // Tablero 3x3.
        Board(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

// Dibuja el tablero a partir de uiState.board.
@Composable
private fun Board(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {
    // Si por lo que sea aún no hay tablero, no dibujamos nada.
    if (uiState.board.isEmpty()) return

    for (r in 0 until uiState.rows) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            for (c in 0 until uiState.cols) {
                val cell = uiState.board[r][c]

                // Desactivo celdas reveladas y también el tablero si la partida terminó.
                val enabledCell = uiState.status == GameStatus.PLAYING && !cell.isRevealed

                // Decidimos el texto visible según el estado de la celda.
                val label = when {
                    !cell.isRevealed -> "■"           // Celda cerrada más realista
                    cell.isMine -> "💣"               // Mina revelada
                    cell.adjacentMines == 0 -> ""     // Sin minas alrededor, no mostramos nada
                    else -> cell.adjacentMines.toString() // Mostramos el número
                }

                CellButton(
                    label = label,
                    isRevealed = cell.isRevealed,  // Para animar solo cuando se revela.
                    onClick = { onEvent(GameEvent.CellPressed(r, c)) },
                    enabled = enabledCell
                )
            }
        }
    }
}

// Botón individual de cada celda, con animación al revelarse.
// Recibe el texto a mostrar, si la celda está revelada, la función a ejecutar al hacer click y si el botón está habilitado.
// La animación hace un pequeño "pop" al revelar la celda
@Composable
private fun CellButton(
    label: String,
    isRevealed: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    // Valor de escala que vamos a animar cuando la celda se revela.
    val scaleAnim = remember { Animatable(1f) }

    // Cuando la celda pasa a revelada, hago el pop: 0.9 -> 1.05 -> 1.0
    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            scaleAnim.snapTo(0.9f)
            scaleAnim.animateTo(1.30f, animationSpec = tween(120))
            scaleAnim.animateTo(1.10f, animationSpec = tween(120))
        }
    }

    Button(
        modifier = Modifier
            .size(56.dp)
            .scale(scaleAnim.value),
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = label)
    }
}