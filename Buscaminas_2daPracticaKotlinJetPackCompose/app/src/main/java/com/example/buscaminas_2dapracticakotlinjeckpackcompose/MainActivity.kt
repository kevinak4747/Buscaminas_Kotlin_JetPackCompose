package com.example.buscaminas_2dapracticakotlinjeckpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.AppDatabase
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.UserRepository
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.nav.AppNavGraph
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.nav.NavRoutes
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.theme.Buscaminas_2daPracticaKotlinJeckpackComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializo la base de datos local de Room
        val db = AppDatabase.getDatabase(applicationContext)

        // Creo los usuarios iniciales en segundo plano
        // Lo hago en IO porque Room trabaja con disco
        lifecycleScope.launch(Dispatchers.IO) {
            UserRepository.insertInitialUsers(db.userDao())
        }

        setContent {
            Buscaminas_2daPracticaKotlinJeckpackComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRoot(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


// AppRoot es la entrada de la interfaz de usuario.
// Desde aquí iremos montando las pantallas y la navegación.
@Composable
fun AppRoot(modifier: Modifier = Modifier) {

    // Creamos el navController una sola vez y Compose lo recuerda.
    // Este objeto es el que usaremos para navegar entre pantallas.
    val navController = rememberNavController()

    // AppNavGraph es el mapa de navegación.
    // Le pasamos el navController para que pueda cambiar entre rutas.
    AppNavGraph(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Buscaminas_2daPracticaKotlinJeckpackComposeTheme {
        AppRoot()
    }
}