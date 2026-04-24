package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api.RemoteRankingPlayer
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components.AppBackground

// Esta pantalla es la bienvenida
// Solo muestra el mensaje inicial, permite empezar la partida y consultar el ranking online
@Composable
fun WelcomeScreen(
    ranking: List<RemoteRankingPlayer>,
    isLoading: Boolean,
    errorMessage: String?,
    onLoadRanking: () -> Unit,
    onNavigateToGame: () -> Unit
) {

    // Uso el mismo fondo que en el resto de pantallas para mantener consistencia visual
    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título principal de la app
            Text(
                text = "Buscaminas",
                color = Color.White,
                fontSize = 32.sp
            )

            // Mensaje de bienvenida
            Text(
                text = "Bienvenido al juego",
                color = Color.White,
                fontSize = 18.sp
            )

            // Botón para empezar la partida
            // Al pulsarlo aviso hacia fuera para navegar a la pantalla de juego
            Button(
                onClick = {
                    onNavigateToGame()
                }
            ) {
                Text(text = "Empezar partida")
            }

            // Botón para consultar el ranking online
            // Al pulsarlo aviso hacia fuera para que el ViewModel haga la llamada a la API
            Button(
                onClick = {
                    onLoadRanking()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Ver ranking online")
            }

            // Solo muestro este contenedor si hay algo que enseñar
            // Puede ser carga, error o datos del ranking
            if (isLoading || errorMessage != null || ranking.isNotEmpty()) {

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)

                        // Limito el ancho para que el contenido no ocupe toda la pantalla
                        .widthIn(min = 260.dp, max = 340.dp)

                        // Fondo oscuro semitransparente para que el texto se lea bien sobre la imagen
                        .background(
                            color = Color(0xAA000000),
                            shape = RoundedCornerShape(16.dp)
                        )

                        // Separación interna para que el texto no quede pegado al borde
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Si se está haciendo la petición, muestro un mensaje de carga
                    if (isLoading) {
                        Text(
                            text = "Cargando ranking...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    // Si hay error, lo muestro dentro del contenedor
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    // Si hay ranking, muestro un pequeño título antes de la lista
                    if (ranking.isNotEmpty()) {
                        Text(
                            text = "Ranking online",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Recorro la lista que llega desde la API y muestro cada jugador
                    ranking.forEach { player ->
                        Text(
                            text = "${player.playerName} - ${player.bestTime} segundos",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}