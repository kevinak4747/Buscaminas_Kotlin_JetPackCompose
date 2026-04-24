package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

// Esta función conecta el ViewModel con la pantalla Welcome
@Composable
fun WelcomeRoute(
    onNavigateToGame: () -> Unit
) {
    // Creo el ViewModel de esta pantalla
    val welcomeViewModel: WelcomeViewModel = viewModel()

    // Escucho los estados del ViewModel para que la UI se actualice sola
    val ranking by welcomeViewModel.ranking.collectAsState()
    val isLoading by welcomeViewModel.isLoading.collectAsState()
    val errorMessage by welcomeViewModel.errorMessage.collectAsState()

    WelcomeScreen(
        ranking = ranking,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onLoadRanking = {
            welcomeViewModel.loadRanking()
        },
        onNavigateToGame = onNavigateToGame
    )
}