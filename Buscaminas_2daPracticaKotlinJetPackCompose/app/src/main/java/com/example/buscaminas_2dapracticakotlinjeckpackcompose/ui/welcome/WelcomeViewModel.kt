package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api.RemoteRankingPlayer
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Este ViewModel se encarga de gestionar la lógica del ranking online
// Aquí es donde hago la llamada a la API y guardo los resultados
class WelcomeViewModel : ViewModel() {

    // Aquí guardo la lista de jugadores que viene de la API
    // Empieza vacía porque todavía no he hecho la llamada
    private val _ranking = MutableStateFlow<List<RemoteRankingPlayer>>(emptyList())
    val ranking = _ranking.asStateFlow()

    // Este estado me sirve para saber si estoy cargando datos
    // Lo usaré para mostrar un "Cargando..." en la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Aquí guardo un posible error si falla la conexión
    // Por ejemplo, si no hay internet
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Esta función se llama cuando quiero pedir el ranking a internet
    fun loadRanking() {

        // Lanzo una corrutina en hilo secundario para no bloquear la interfaz
        viewModelScope.launch(Dispatchers.IO) {

            // Activo la carga para que la pantalla muestre el mensaje de búsqueda
            _isLoading.value = true

            // Limpio errores anteriores antes de hacer una nueva consulta
            _errorMessage.value = null

            // Limpio el ranking anterior para que se note que estoy haciendo una nueva petición
            _ranking.value = emptyList()

            try {
                // Hago la llamada real a la API usando Retrofit
                val response = RetrofitClient.apiService.getRankingBuscaminas()

                // Guardo el ranking recibido para que la pantalla se actualice
                _ranking.value = response

            } catch (e: Exception) {

                // Si falla la llamada, dejo la lista vacía y muestro el error
                _ranking.value = emptyList()
                _errorMessage.value = "No se puede consultar el ranking online, verifique su internet"
            }

            // Desactivo la carga cuando termina la petición
            _isLoading.value = false
        }
    }
}