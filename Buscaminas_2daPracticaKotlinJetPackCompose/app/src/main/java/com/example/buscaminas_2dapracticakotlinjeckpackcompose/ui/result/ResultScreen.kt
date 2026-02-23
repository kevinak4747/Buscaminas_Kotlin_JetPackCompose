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
import androidx.compose.ui.unit.dp

// Esta pantalla representa el resultado final de la partida.
// La utilizo como cuarta pantalla del proyecto para cumplir el requisito


@Composable
fun ResultScreen(
    // title recibe el texto que se mostrará arriba.
    // Puede ser "Has ganado" o "Has perdido".
    title: String,

    // onPlayAgain es una función que se ejecuta cuando el usuario
    // quiere empezar una nueva partida.
    onPlayAgain: () -> Unit,

    // onBackToMenu es la función que permite volver a la pantalla anterior
    onBackToMenu: () -> Unit
) {

    // Column organiza los elementos en vertical.
    // Es el contenedor principal de la pantalla.
    Column(

        // fillMaxSize ocupa toda la pantalla.
        // padding añade espacio interior para que no quede pegado a los bordes.
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        // spacedBy separa los elementos verticalmente.
        verticalArrangement = Arrangement.spacedBy(12.dp),

        // Centra horizontalmente los elementos.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Mostramos el título recibido como parámetro.
        // Uso un estilo headline para que destaque visualmente.
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        // Botón para reiniciar la partida.
        // Solo llama a la función recibida, no tiene lógica propia.
        Button(
            onClick = onPlayAgain
        ) {
            Text("Jugar otra")
        }

        // Botón para volver al menú o pantalla anterior.
        Button(
            onClick = onBackToMenu
        ) {
            Text("Volver al menú")
        }
    }
}