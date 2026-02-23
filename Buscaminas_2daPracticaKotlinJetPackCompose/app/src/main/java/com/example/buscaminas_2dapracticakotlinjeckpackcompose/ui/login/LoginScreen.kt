package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Esta pantalla solo se encarga de mostrar el formulario de login.
// No hace lógica de validación ni decide nada del juego.
@Composable
fun LoginScreen(
    userName: String,
    password: String,
    // errorMessage es un texto que se muestra si el login no es correcto. Puede ser null si no hay error.
    errorMessage: String?,
    // es una lamba que recibe el nuevo nombre de usuario cuando el usuario escribe en el campo de texto y
    // no devuelve nada (Unit)
    onUserNameChange: (String) -> Unit,
    // es una lambda que recibe el nuevo password cuando el usuario escribe en el campo de texto y
    // no devuelve nada (Unit)
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit


    // es una lambda que se llama cuando el usuario hace click en el botón de continuar y no devuelve nada (Unit)
    // ni recibe ningún parámetro

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Título simple para saber que estamos en la pantalla de login.
        Text(text = "Login")

        // Campo donde el usuario escribe su nombre. en el OutlinedTextField
        // La pantalla no guarda el estado, solo muestra el valor y avisa cuando cambia.
        OutlinedTextField(
            value = userName,
            onValueChange = { newValue ->
                // Cuando el usuario escribe, avisamos hacia fuera con la función onUserNameChange.
                onUserNameChange(newValue)
            },
            // El campo de texto ocupa todo el ancho disponible y tiene una etiqueta.
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "UserName") }, // lo que pone UserName es como un placeholder dentro del campo de texto, pero se mueve hacia arriba cuando el usuario escribe.
            singleLine = true
        )
        // Campo donde el usuario escribe la contraseña.
        // No guardo estado aquí, solo muestro el valor y aviso cuando cambia.
        OutlinedTextField(
            value = password,
            onValueChange = { newValue ->
                onPasswordChange(newValue)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Password") },
            singleLine = true,  // Solo permito una línea como en cualquier login real
            visualTransformation = PasswordVisualTransformation()  // Oculta el texto con puntos
        )

        // Si hay un mensaje de error (login incorrecto), lo mostramos debajo de los campos de texto.
        if (errorMessage != null) {
            Text(text = errorMessage)
        }

        // Botón para continuar.
        // La pantalla no navega por sí sola, solo avisa hacia fuera con onLoginClick.
        Button(
            onClick = {
                onLoginClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }
    }
}