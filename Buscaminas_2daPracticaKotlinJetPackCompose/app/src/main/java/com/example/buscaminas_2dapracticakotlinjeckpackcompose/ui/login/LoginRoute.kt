package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

// Esta función conecta LoginScreen con LoginViewModel y con la navegación.
@Composable
fun LoginRoute(
    onNavigateToWelcome: () -> Unit
) {
    // Creo/obtengo el ViewModel asociado a esta pantalla.
    val vm: LoginViewModel = viewModel()

    // Observo el estado del ViewModel para que la UI se actualice sola.
    val state by vm.uiState.collectAsState()

    // Cuando el login es correcto, navego a Welcome.
    // Lo hago con LaunchedEffect para no navegar en cada recomposición.
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
            vm.onEvent(LoginEvent.LoginPressed)
        }
    )
}