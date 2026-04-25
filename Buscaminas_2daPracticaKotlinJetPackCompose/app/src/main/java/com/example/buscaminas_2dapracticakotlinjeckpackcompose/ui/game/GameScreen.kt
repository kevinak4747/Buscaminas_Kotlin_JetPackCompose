package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.zIndex
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components.AppBackground

// GameScreen es una pantalla "tonta":
// solo dibuja el estado y envía eventos al ViewModel.
@Composable
fun GameScreen(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    onBackToMenu: () -> Unit
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


    // Uso el mismo fondo para toda la pantalla, así se mantiene consistente en todas las pantallas del juego
    AppBackground {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Esta capa añade un color suave encima del fondo cuando se gana o se pierde
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        animatedBackgroundColor.copy(
                            alpha = if (
                                uiState.status == GameStatus.WON ||
                                uiState.status == GameStatus.LOST
                            )   // Si el juego se ha ganado o perdido, aplico un color semitransparente para
                                // destacar el resultado, si no, no aplico color (alpha 0)
                                0.75f else 0f
                        )
                    )
            )


            // Contenido principal de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Título de pantalla.
                // Lo pongo en blanco para que se vea bien sobre el fondo oscuro
                Text(
                    text = "Buscaminas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                // Muestro el estado actual de la partida
                // También lo pongo en blanco para que no se pierda con el fondo
                Text(
                    text = "Estado: ${uiState.status}",
                    color = Color.White
                )

                // Muestro el tiempo actual de la partida en segundos
                Text(
                    text = "Tiempo: ${uiState.elapsedSeconds} s",
                    color = Color.White
                )

                // Agrupo los botones de acción de la partida en una fila
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Botón para reiniciar la partida
                    // Solo lo permito si la partida está en curso
                    Button(
                        onClick = { onEvent(GameEvent.RestartPressed) },
                        enabled = uiState.status == GameStatus.PLAYING
                    ) {
                        Text(text = "Reiniciar")
                    }

                    // Botón para pausar manualmente la partida
                    // Solo lo permito si la partida está en curso
                    Button(
                        onClick = { onEvent(GameEvent.PausePressed) },
                        enabled = uiState.status == GameStatus.PLAYING
                    ) {
                        Text(text = "Pausar")
                    }

                    // Botón para volver a la pantalla de bienvenida
                    // Lo uso para salir de la partida y regresar al menú
                    Button(
                        onClick = onBackToMenu,
                        enabled = uiState.status == GameStatus.PLAYING
                    ) {
                        Text(text = "Volver")
                    }
                }

                // Dibujo el tablero del juego usando un LazyVerticalGrid
                Board(
                    uiState = uiState,
                    onEvent = onEvent
                )
            }
            // Si la partida está en pausa, dibujo una capa encima de la pantalla
            if (uiState.showPauseOverlay) {
                PauseOverlay(
                    onResumeClick = {
                        // Cuando el usuario pulsa reanudar, mando el evento al ViewModel
                        onEvent(GameEvent.ResumePressed)
                    }
                )
            }
        }
    }
}

@Composable
private fun PauseOverlay(
    // función que se llama cuando el usuario pulsa el botón de reanudar
    onResumeClick: () -> Unit
) {

    // Capa que cubre toda la pantalla para bloquear la interacción con el juego
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.97f)),
        contentAlignment = Alignment.Center
    ) {

        // Contenedor central con el mensaje de pausa y el botón
        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Texto que indica al usuario que la partida está pausada
            Text(
                text = "Partida en pausa",
                style = MaterialTheme.typography.titleLarge
            )

            // Botón para reanudar la partida
            // Al pulsarlo se lanza el evento hacia el ViewModel
            Button(onClick = onResumeClick) {
                Text(text = "Reanudar")
            }
        }
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

    // Añado margen arriba y lateral para que el tablero respire y no quede pegado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(uiState.cols),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 6.dp)
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
                    adjacentMines = cell.adjacentMines,
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
}

// Dibuja una casilla individual del tablero
// Detecta pulsación normal para abrir y pulsación larga para bandera
// También aplica una pequeña animación al revelarse
@Composable
private fun CellButton(
    label: String,
    adjacentMines: Int,
    isRevealed: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
){

    // Valor de escala que uso para la animación al revelar
    // Inicialmente es 1 (tamaño normal) y luego lo cambio para hacer el efecto pop
    val scaleAnim = remember { Animatable(1f) }

    // Cuando la casilla pasa a revelada hago un pequeño efecto pop más suave
    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            // Hago una animación pequeña para que no se monte con las casillas vecinas
            scaleAnim.snapTo(0.96f)
            scaleAnim.animateTo(1.08f, animationSpec = tween(100))
            scaleAnim.animateTo(1f, animationSpec = tween(100))
        }
    }


    // Decido el color del número según la cantidad de minas vecinas
    // Esto lo calculo siempre, pero solo se verá cuando la casilla esté revelada
    val numberColor = when (adjacentMines) {
        1 -> Color(0xFF2563EB) // Azul
        2 -> Color(0xFF16A34A) // Verde
        3 -> Color(0xFFDC2626) // Rojo
        4 -> Color(0xFF7C3AED) // Morado
        else -> Color(0xFF111827) // Oscuro
    }

    // Uso un Box para dibujar la casilla en lugar de Button
    Box(
        modifier = Modifier
            // Tamaño fijo de cada celda
            .size(52.dp)
            // Aplico la animación de escala
            .scale(scaleAnim.value)
            // Si la casilla está revelada, la dibujo por encima para que no se vea cortada
            .zIndex(if (isRevealed) 1f else 0f)
            // Borde de la casilla
            .border(
                BorderStroke(1.dp, Color(0xFFD8CCB8)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )

            // Color según si está revelada o no
            .background(
                color = if (isRevealed) Color(0xFFF8F4EC) else Color(0xFF566C9E),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
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
            // Si la casilla está cerrada uso blanco, si está abierta decido el color según su contenido
            color = when {
                !isRevealed -> Color.White
                label == "💣" -> Color.Black
                label == "🚩" -> Color.Black
                else -> numberColor
            }
        )
    }
}