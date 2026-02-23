package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Esta pantalla es la bienvenida.
// De momento solo sirve para comprobar que la navegación funciona.
@Composable
fun WelcomeScreen(
    onNavigateToGame: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Welcome")

        Button(
            onClick = { onNavigateToGame() }
        ) {
            Text(text = "Empezar partida")
        }
    }
}