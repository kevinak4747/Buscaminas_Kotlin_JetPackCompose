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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

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

        // Muestro el tiempo actual de la partida en segundos
        Text(text = "Tiempo: ${uiState.elapsedSeconds} s")

        // Botón para reiniciar (manda evento al ViewModel).
        Button(onClick = { onEvent(GameEvent.RestartPressed) }) {
            Text(text = "Reiniciar")
        }

        // Tablero 8x8 con 8 minas, se dibuja con un LazyVerticalGrid.
        Board(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

// Dibuja el tablero usando LazyVerticalGrid
@Composable
private fun Board(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit
) {
    // Si por lo que sea aún no hay tablero, no dibujo nada
    if (uiState.board.isEmpty()) return

    // Convierto la matriz de casillas en una sola lista
    // Esto me permite pintar todas las celdas dentro del grid
    val flatBoard = uiState.board.flatten()

    LazyVerticalGrid(
        columns = GridCells.Fixed(uiState.cols),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flatBoard) { cell ->

            // Solo permito interactuar si la partida sigue en curso
            val enabledCell = uiState.status == GameStatus.PLAYING

            // Decido qué texto mostrar según el estado de la casilla
            val label = when {

                // Si tiene bandera y no está revelada muestro bandera
                cell.hasFlag && !cell.isRevealed -> "🚩"

                // Si está cerrada muestro el bloque
                !cell.isRevealed -> "■"

                // Si es mina y está revelada muestro la bomba
                cell.isMine -> "💣"

                // Si no tiene minas alrededor no muestro nada
                cell.adjacentMines == 0 -> ""

                // Si tiene minas alrededor muestro el número
                else -> cell.adjacentMines.toString()
            }

            // Dibujo la casilla y le paso sus coordenadas reales
            CellButton(
                label = label,
                isRevealed = cell.isRevealed,
                enabled = enabledCell,
                onClick = {
                    onEvent(GameEvent.CellPressed(cell.row, cell.col))
                },
                onLongClick = {
                    onEvent(GameEvent.CellLongPressed(cell.row, cell.col))
                }
            )
        }
    }
}

// Dibuja una casilla individual del tablero
// Detecta pulsación normal para abrir y pulsación larga para bandera
// También aplica una pequeña animación al revelarse
@Composable
private fun CellButton(
    label: String,
    isRevealed: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {

    // Valor de escala que uso para la animación al revelar
    // Inicialmente es 1 (tamaño normal) y luego lo cambio para hacer el efecto pop
    val scaleAnim = remember { Animatable(1f) }

    // Cuando la casilla pasa a revelada hago un pequeño efecto pop
    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            // Hago un pequeño "pop" al revelar: primero achico un poco, luego agrando y
            // luego vuelvo a tamaño normal
            scaleAnim.snapTo(0.9f)
            scaleAnim.animateTo(1.30f, animationSpec = tween(120))
            scaleAnim.animateTo(1.10f, animationSpec = tween(120))
        }
    }

    // Uso un Box para dibujar la casilla en lugar de Button
    Box(
        modifier = Modifier
            // Tamaño fijo de cada celda
            .size(56.dp)
            // Aplico la animación de escala
            .scale(scaleAnim.value)
            // Borde de la casilla
            .border(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = MaterialTheme.shapes.small
            )

            // Color según si está revelada o no
            .background(
                color = if (isRevealed) Color(0xFFE0E0E0)
                else MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )

            // Detecto click normal y pulsación larga
            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick
            ),
        // Centro el contenido dentro de la casilla
        contentAlignment = Alignment.Center
    ) {

        // Texto de la casilla (mina, número, bandera o vacío)
        Text(
            text = label,
            // Cambio color según esté revelada o no
            color = if (isRevealed) Color.Black else Color.White
        )
    }
}