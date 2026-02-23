package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.nav.NavRoutes
import kotlinx.coroutines.delay

// GameRoute conecta el ViewModel con la pantalla.
// Recoge el estado y pasa una función para enviar eventos.
@Composable
fun GameRoute(
    // NavController para poder navegar a otras pantallas.
    // Lo recibo como parámetro porque lo necesito para navegar a la pantalla de resultado.
    // En esta pantalla no necesito navegar a otras, pero lo dejo preparado para cuando sí lo necesite.
    navController: NavHostController,
    gameViewModel: GameViewModel = viewModel()
) {
    // Observamos el estado del ViewModel para que la UI se redibuje.
    val uiState = gameViewModel.uiState.collectAsState().value

    // Si la partida termina, guardo el resultado y navego a RESULT.
    LaunchedEffect(uiState.status) {

        if (uiState.status == GameStatus.WON || uiState.status == GameStatus.LOST) {

            // Espero 2 segundos para que el jugador vea el tablero final.
            delay(3000)

            // Guardo el resultado en la entrada actual de navegacióncon el
            //WON/LOST en savedStateHandle para que RESULT pueda leerlo
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("game_result", uiState.status.name)

            // Navego a la pantalla de resultado.
            navController.navigate(NavRoutes.RESULT)
        }
    }

    // Dibujamos la pantalla con el estado actual y la función de eventos.
    GameScreen(
        uiState = uiState,
        onEvent = gameViewModel::onEvent
    )
}