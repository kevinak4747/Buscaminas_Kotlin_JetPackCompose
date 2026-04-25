package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components.AppBackground



// Esta pantalla representa el resultado final de la partida.
// La utilizo como cuarta pantalla del proyecto para cumplir el requisito
@Composable
fun ResultScreen(
    // title recibe el texto que se mostrará arriba.
    // Puede ser "Has ganado" o "Has perdido".
    title: String,

    // onPlayAgain se ejecuta cuando el usuario quiere empezar una nueva partida
    onPlayAgain: () -> Unit,

    // onBackToMenu permite volver al menú o pantalla anterior
    onBackToMenu: () -> Unit
) {

    // Uso el mismo fondo que en el resto de pantallas para mantener consistencia
    AppBackground {

        // Column organiza los elementos en vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Muestro el título del resultado (ganar o perder)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            // Botón para empezar otra partida
            Button(
                onClick = onPlayAgain
            ) {
                Text("Jugar otra")
            }

            // Botón para volver al menú
            Button(
                onClick = onBackToMenu
            ) {
                Text("Volver al menú")
            }
        }
    }
}