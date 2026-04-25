package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.UserDao
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Estado de la pantalla de login
// Aquí guardo lo que escribe el usuario y el resultado del login
data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val isLogged: Boolean = false,
    val errorMessage: String? = null
)

// Eventos que la UI envía al ViewModel
sealed class LoginEvent {

    // Evento cuando cambia el usuario
    data class UserNameChanged(val value: String) : LoginEvent()

    // Evento cuando cambia la contraseña
    data class PasswordChanged(val value: String) : LoginEvent()

    // Evento cuando se pulsa login
    // Paso el DAO porque ahora necesito acceder a la base de datos
    data class LoginPressed(val dao: UserDao) : LoginEvent()
}

class LoginViewModel : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado público que observa la UI
    val uiState: StateFlow<LoginUiState> = _uiState

    // Función principal que recibe todos los eventos de la UI
    fun onEvent(event: LoginEvent) {
        when (event) {

            // Actualizo el username cuando el usuario escribe
            is LoginEvent.UserNameChanged -> {
                val current = _uiState.value
                _uiState.value = current.copy(
                    userName = event.value,
                    errorMessage = null
                )
            }

            // Actualizo la contraseña cuando el usuario escribe
            is LoginEvent.PasswordChanged -> {
                val current = _uiState.value
                _uiState.value = current.copy(
                    password = event.value,
                    errorMessage = null
                )
            }

            // Cuando se pulsa login, valido usando Room
            is LoginEvent.LoginPressed -> {
                validateLogin(event.dao)
            }
        }
    }

    // Lógica del login usando base de datos
    private fun validateLogin(dao: UserDao) {

        // Guardo el estado actual antes de ir al hilo secundario
        val current = _uiState.value

        // Lanzo una corrutina en el hilo de IO
        // porque leer de base de datos no puede hacerse en el hilo principal
        viewModelScope.launch(Dispatchers.IO) {

            // Busco el usuario en la base de datos
            val user = UserRepository.getUser(current.userName, dao)

            // Vuelvo al hilo principal para actualizar la UI
            withContext(Dispatchers.Main) {

                // Si el usuario existe y la contraseña coincide
                if (user != null && user.password == current.password) {

                    // Login correcto
                    _uiState.value = current.copy(
                        isLogged = true,
                        errorMessage = null
                    )

                } else {

                    // Login incorrecto
                    _uiState.value = current.copy(
                        isLogged = false,
                        errorMessage = "Credenciales incorrectas"
                    )
                }
            }
        }
    }
}