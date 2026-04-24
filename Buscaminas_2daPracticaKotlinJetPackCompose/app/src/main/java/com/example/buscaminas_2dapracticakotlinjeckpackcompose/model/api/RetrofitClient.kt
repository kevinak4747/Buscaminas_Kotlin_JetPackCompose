package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Este objeto configura Retrofit y crea una única instancia para toda la app
object RetrofitClient {

    // Base de la URL (SIEMPRE acaba en /)
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    // Creamos el servicio de la API
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // GsonConverterFactory convierte automáticamente el JSON de la respuesta en objetos de Kotlin
            .addConverterFactory(GsonConverterFactory.create())
            // build() crea la instancia de Retrofit y create() crea la implementación de ApiService
            .build()
            .create(ApiService::class.java)
    }
}