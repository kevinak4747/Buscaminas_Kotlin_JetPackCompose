package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.AppDatabase

// Esta función conecta LoginScreen con LoginViewModel, Room y la navegación
@Composable
fun LoginRoute(
    onNavigateToWelcome: () -> Unit
) {
    // Obtengo el ViewModel asociado a esta pantalla
    val vm: LoginViewModel = viewModel()

    // Obtengo el contexto actual de la app
    // Lo necesito para poder acceder a la base de datos de Room
    val context = LocalContext.current

    // Obtengo el DAO de usuarios desde la base de datos
    // Este DAO permite buscar usuarios guardados en Room
    val userDao = AppDatabase.getDatabase(context).userDao()

    // Observo el estado del ViewModel para que la UI se actualice sola
    val state by vm.uiState.collectAsState()

    // Cuando el login es correcto, navego a Welcome
    // Lo hago con LaunchedEffect para no navegar en cada recomposición
    LaunchedEffect(state.isLogged) {
        if (state.isLogged) {
            onNavigateToWelcome()
        }
    }

    LoginScreen(
        userName = state.userName,
        password = state.password,
        errorMessage = state.errorMessage,

        onUserNameChange = { newValue ->
            vm.onEvent(LoginEvent.UserNameChanged(newValue))
        },

        onPasswordChange = { newValue ->
            vm.onEvent(LoginEvent.PasswordChanged(newValue))
        },

        onLoginClick = {
            // Cuando se pulsa login, envío el evento al ViewModel
            // Ahora también le paso el DAO para consultar la base de datos
            vm.onEvent(LoginEvent.LoginPressed(userDao))
        }
    )
}