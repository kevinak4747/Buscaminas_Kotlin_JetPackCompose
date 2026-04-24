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
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.audio.BackgroundMusicPlayer
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.audio.GameSoundPlayer

// GameRoute conecta el ViewModel con la pantalla
// Recoge el estado y pasa una función para enviar eventos
@Composable
fun GameRoute(
    // NavController para poder navegar a otras pantallas
    // Lo recibo porque lo necesito para ir a la pantalla de resultado al terminar la partida
    navController: NavHostController,
    gameViewModel: GameViewModel = viewModel()
) {
    // Observo el estado del ViewModel para que la UI se redibuje cuando cambie
    val uiState = gameViewModel.uiState.collectAsState().value

    // Obtengo el contexto para crear objetos que necesitan acceso a Android
    val context = LocalContext.current

    // Obtengo el ciclo de vida de esta pantalla para reaccionar cuando la app se pausa o se reanuda
    val lifecycleOwner = LocalLifecycleOwner.current

    // Creo el detector del shake una sola vez mientras esta pantalla siga viva
    val shakeDetector = remember {
        GameShakeDetector(context) {
            gameViewModel.onEvent(GameEvent.RestartPressed)
        }
    }

    // Creo el reproductor de música una sola vez mientras esta pantalla siga viva
    val backgroundMusicPlayer = remember {
        BackgroundMusicPlayer(context)
    }

    // Creo el reproductor de efectos una sola vez mientras esta pantalla siga viva
    val gameSoundPlayer = remember {
        GameSoundPlayer(context)
    }

    // Este bloque escucha cambios del ciclo de vida mientras esta pantalla está activa
    DisposableEffect(lifecycleOwner, shakeDetector, backgroundMusicPlayer, gameSoundPlayer) {

        // Preparo la música de fondo una sola vez al entrar en esta pantalla
        backgroundMusicPlayer.prepare()

        // Creo un observer para reaccionar a eventos como onResume y onPause
        // El guion bajo indica que ese primer parámetro no lo voy a usar
        val observer = LifecycleEventObserver { _, event ->

            when (event) {

                // Cuando la pantalla vuelve al primer plano, activo el acelerómetro
                // y reanudo la música de fondo
                Lifecycle.Event.ON_RESUME -> {
                    shakeDetector.startListening()

                    if (gameViewModel.uiState.value.status == GameStatus.PLAYING) {
                        backgroundMusicPlayer.start()
                    }
                }

                // Cuando la app se pausa, paro el sensor, pauso la música
                // y además pauso la partida
                Lifecycle.Event.ON_PAUSE -> {
                    shakeDetector.stopListening()
                    backgroundMusicPlayer.pause()
                    gameViewModel.onEvent(GameEvent.AppPaused)
                }

                else -> Unit
            }
        }

        // Añado el observer al ciclo de vida
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cuando este composable se destruye, elimino el observer y libero la música
        onDispose {
            shakeDetector.stopListening()
            backgroundMusicPlayer.release()
            gameSoundPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Si hay un sonido pendiente, lo reproduzco una sola vez y después lo limpio del estado
    LaunchedEffect(uiState.pendingSoundEffect) {
        when (uiState.pendingSoundEffect) {
            GameSoundEffect.REVEAL -> gameSoundPlayer.playReveal()
            GameSoundEffect.EXPANSION -> gameSoundPlayer.playExpansion()
            GameSoundEffect.FLAG -> gameSoundPlayer.playFlag()
            GameSoundEffect.BOMB -> gameSoundPlayer.playBomb()
            GameSoundEffect.WIN -> gameSoundPlayer.playWin()
            GameSoundEffect.LOSE -> gameSoundPlayer.playLose()
            null -> Unit
        }

        if (uiState.pendingSoundEffect != null) {
            gameViewModel.onEvent(GameEvent.SoundEffectConsumed)
        }
    }

    // Este bloque se ejecuta cada vez que cambia el estado de la partida
    // Lo uso para controlar la música según si el juego está en curso, en pausa o terminado
    LaunchedEffect(uiState.status) {

        when (uiState.status) {

            // Si la partida está en curso, la música debe estar sonando
            GameStatus.PLAYING -> {
                backgroundMusicPlayer.start()
            }

            // Si la partida está en pausa, paro la música
            GameStatus.PAUSED -> {
                backgroundMusicPlayer.pause()
            }

            // Si la partida ha terminado (ganar o perder)
            GameStatus.WON, GameStatus.LOST -> {

                // Paro la música antes de salir de la pantalla
                backgroundMusicPlayer.pause()

                // Si se pierde, reproduzco el sonido final de derrota
                if (uiState.status == GameStatus.LOST) {
                    gameSoundPlayer.playLose()
                }

                // Espero unos segundos para que el jugador vea el resultado final
                delay(3000)

                // Guardo el resultado para que la siguiente pantalla lo pueda leer
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("game_result", uiState.status.name)

                // Navego a la pantalla de resultado
                navController.navigate(NavRoutes.RESULT)
            }
        }
    }

    // Si empieza una partida nueva, reinicio la música desde el principio
    // Lo detecto cuando el tiempo vuelve a 0 y la partida está en curso
    LaunchedEffect(uiState.elapsedSeconds, uiState.status) {
        if (uiState.elapsedSeconds == 0 && uiState.status == GameStatus.PLAYING) {
            backgroundMusicPlayer.restart()
        }
    }


    // Dibujo la pantalla con el estado actual y la función para enviar eventos
    GameScreen(
        uiState = uiState,
        onEvent = gameViewModel::onEvent
    )
}