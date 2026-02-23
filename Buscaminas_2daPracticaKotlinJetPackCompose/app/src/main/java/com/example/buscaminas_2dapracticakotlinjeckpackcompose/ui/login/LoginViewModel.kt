package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Estado de la pantalla de login.
// Aquí guardo lo que el usuario escribe y el resultado del login.
data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val isLogged: Boolean = false,
    val errorMessage: String? = null
)

// Eventos que la UI le manda al ViewModel.
sealed class LoginEvent {
    data class UserNameChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object LoginPressed : LoginEvent()
}

class LoginViewModel : ViewModel() {

    // Estado interno mutable.
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado público de solo lectura.
    val uiState: StateFlow<LoginUiState> = _uiState

    // Lista de usuarios hardcodeados para poder entrar sin registro.
    // La clave es el userName y el valor es la contraseña.
    private val demoUsers = mapOf(
        "kevin" to "1234",
        "pedro" to "1234"
    )

    // Punto único de entrada de eventos desde la UI.
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UserNameChanged -> {
                val current = _uiState.value
                _uiState.value = current.copy(
                    userName = event.value,
                    errorMessage = null
                )
            }

            is LoginEvent.PasswordChanged -> {
                val current = _uiState.value
                _uiState.value = current.copy(
                    password = event.value,
                    errorMessage = null
                )
            }

            LoginEvent.LoginPressed -> {
                validateLogin()
            }
        }
    }

    // Lógica del login.
// Compruebo si el userName existe y si la contraseña coincide.
    private fun validateLogin() {
        // Leemos el estado actual.
        val current = _uiState.value
        // Buscamos la contraseña guardada para el userName que el usuario ha escrito.
        // Si el userName no existe, savedPassword será null.
        val savedPassword = demoUsers[current.userName]
        // El login es correcto si el userName existe (savedPassword no es null) y la contraseña coincide.
        val loginCorrect = savedPassword != null && savedPassword == current.password
        // Actualizamos el estado según el resultado del login.
        if (loginCorrect) {
            _uiState.value = current.copy(
                isLogged = true,
                errorMessage = null
            )
        }
        // Si el login no es correcto, marcamos isLogged como false y ponemos un mensaje de error.
        else {
            _uiState.value = current.copy(
                isLogged = false,
                errorMessage = "Credenciales incorrectas"
            )
        }
    }
}
