package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.nav.NavRoutes
import kotlinx.coroutines.delay
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

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

    // Obtengo el contexto para poder crear el detector del acelerómetro
    val context = LocalContext.current

    // Obtengo el ciclo de vida de esta pantalla para detectar cuándo la app se pausa
    val lifecycleOwner = LocalLifecycleOwner.current

    // Creo el detector del shake una sola vez mientras esta pantalla siga viva
    val shakeDetector = remember {
        GameShakeDetector(context) {
            gameViewModel.onEvent(GameEvent.RestartPressed)
        }
    }

    // Este bloque escucha cambios del ciclo de vida mientras esta pantalla está activa
    DisposableEffect(lifecycleOwner, shakeDetector) {

        // Creo un observer para reaccionar a eventos como onPause
        // El guion bajo indica que ese primer parámetro no lo voy a usar
        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                // Cuando la pantalla vuelve al primer plano, activo el acelerómetro
                Lifecycle.Event.ON_RESUME -> {
                    shakeDetector.startListening()
                }

                // Cuando la app se pausa, paro el sensor y además pauso la partida
                Lifecycle.Event.ON_PAUSE -> {
                    shakeDetector.stopListening()
                    gameViewModel.onEvent(GameEvent.AppPaused)
                }

                else -> Unit
            }
        }

        // Añado el observer al ciclo de vida
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cuando este composable se destruye, elimino el observer para evitar fugas
        onDispose {
            shakeDetector.stopListening()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Si la partida termina, guardo el resultado y navego a RESULT.
    LaunchedEffect(uiState.status) {

        if (uiState.status == GameStatus.WON || uiState.status == GameStatus.LOST) {

            // Espero 3 segundos para que el jugador vea el tablero final.
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