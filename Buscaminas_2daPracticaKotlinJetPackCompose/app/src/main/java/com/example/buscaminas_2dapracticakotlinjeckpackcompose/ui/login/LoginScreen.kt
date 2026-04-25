package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components.AppBackground

// Esta pantalla solo se encarga de mostrar el formulario de login
// No contiene lógica, solo pinta la UI y envía eventos hacia fuera
@Composable
fun LoginScreen(
    userName: String,
    password: String,
    errorMessage: String?,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {

    // Uso el fondo común para mantener el mismo estilo en toda la app
    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título principal de la app para darle más identidad visual
            Text(
                text = "Buscaminas",
                color = Color.White,
                fontSize = 28.sp
            )

            // Subtítulo para indicar que estamos en el login
            Text(
                text = "Login",
                color = Color.White
            )

            // Campo donde el usuario escribe el nombre
            // No guardo estado aquí, solo muestro el valor y aviso cuando cambia
            OutlinedTextField(
                value = userName,
                onValueChange = { newValue ->
                    onUserNameChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("UserName", color = Color.White) },
                singleLine = true,

                // Configuro los colores para que se vean bien sobre fondo oscuro
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            // Campo donde el usuario escribe la contraseña
            // Oculto el texto con puntos como en cualquier login real
            OutlinedTextField(
                value = password,
                onValueChange = { newValue ->
                    onPasswordChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password", color = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),

                // Mismos colores para mantener coherencia visual
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            // Si hay error en el login lo muestro debajo de los campos
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF6B6B) // rojo suave para destacar error
                )
            }

            // Botón de login
            // No navega directamente, solo lanza el evento hacia fuera
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
}