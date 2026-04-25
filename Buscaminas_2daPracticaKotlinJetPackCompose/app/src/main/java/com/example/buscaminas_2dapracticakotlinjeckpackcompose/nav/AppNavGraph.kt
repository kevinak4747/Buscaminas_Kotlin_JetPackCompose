package com.example.buscaminas_2dapracticakotlinjeckpackcompose.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game.GameRoute
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game.GameStatus

// Importo LoginRoute para poder dibujar la pantalla de login y conectar la navegación.
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login.LoginRoute
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.result.ResultRoute
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome.WelcomeRoute


// Este archivo define el mapa de navegación de la app.
// Aquí se declaran las rutas y qué pantalla se dibuja en cada una.
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = NavRoutes.LOGIN
) {

    // NavHost es el contenedor principal de navegación.
    // Es el que decide qué pantalla se muestra en cada momento.
    NavHost(
        navController = navController,         // Le pasamos el controlador que permite cambiar de pantalla.
        startDestination = startDestination    // Indicamos cuál será la primera pantalla al abrir la app.
    ) {

        // Si la ruta actual es LOGIN, aquí se dibujará la pantalla Login.
        composable(NavRoutes.LOGIN) {
            // pantalla Login()

            // Cuando LoginRoute me avisa, navego a la pantalla Welcome
            LoginRoute(
                onNavigateToWelcome = {
                    navController.navigate(NavRoutes.WELCOME)
                }
            )
        }

        // Aquí irá la pantalla Welcome()
        composable(NavRoutes.WELCOME) {
            // Aquí irá la pantalla Welcome()
            // Cuando el usuario pulsa empezar, navegamos a la pantalla del juego.
            WelcomeRoute(
                onNavigateToGame = {
                    navController.navigate(NavRoutes.GAME)
                }
            )
        }

        // Si la ruta es GAME, se mostrará la pantalla del juego.
        composable(NavRoutes.GAME) {
            // Cuando la ruta GAME está activa, se dibuja GameRoute
            // y le paso el navController para poder navegar a otras pantallas.
            GameRoute(navController = navController)

        }

        // Si la ruta es RESULT, se mostrará la pantalla final.
        composable(NavRoutes.RESULT) {

            // Leo el resultado que dejó la pantalla GAME.
            val result = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("game_result")

            // Decido el título según si ganó o perdió.
            val resultGame = if (result == GameStatus.WON.name) {
                "Has ganado 😀"
            } else {
                "Has perdido 😞"
            }

            ResultRoute(
                title = "Fin de partida\n" + resultGame, // Aquí se muestra el resultado de la partida.
                onPlayAgain = {
                    //esta es la lambda que se ejecuta al pulsar "Jugar otra". Aquí es donde reiniciamos la partida.
                    // llamada desde ResultRoute, que a su vez se llama desde ResultScreen.
                    navController.navigate(NavRoutes.GAME) {
                        popUpTo(NavRoutes.GAME) { inclusive = true }
                    }
                },
                // Vuelvo a Welcome
                //lambda que se ejecuta al pulsar "Volver al menú". Aquí es donde volvemos a la pantalla de bienvenida.
                // llamada desde ResultRoute, que a su vez se llama desde ResultScreen.
                onBackToMenu = {
                    navController.navigate(NavRoutes.WELCOME) {
                        popUpTo(NavRoutes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
    }
}