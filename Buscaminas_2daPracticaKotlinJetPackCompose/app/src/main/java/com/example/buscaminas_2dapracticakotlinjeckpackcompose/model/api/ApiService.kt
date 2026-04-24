package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api

import retrofit2.http.GET

// Esta interfaz define la llamada que hará Retrofit al JSON remoto
interface ApiService {

    // Esta ruta es la parte de la URL que queda después de https://raw.githubusercontent.com/
    @GET("kevinak4747/buscaminas-api/refs/heads/main/ranking_buscaminas.json")
    suspend fun getRankingBuscaminas(): List<RemoteRankingPlayer>
}