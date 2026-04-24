package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.R

// Este composable me sirve para poner el mismo fondo en varias pantallas
@Composable
fun AppBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Pinto la imagen de fondo ocupando toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.background_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Encima de la imagen coloco el contenido de cada pantalla
        content()
    }
}