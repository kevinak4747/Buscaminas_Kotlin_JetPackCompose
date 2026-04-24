package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components.AppBackground

// Esta pantalla es la bienvenida
// Solo muestra el mensaje inicial y permite empezar la partida
@Composable
fun WelcomeScreen(
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
        }
    }
}