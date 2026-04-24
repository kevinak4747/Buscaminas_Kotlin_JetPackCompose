package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model.api

// Este data class representa cada jugador que viene del JSON remoto
data class RemoteRankingPlayer(
    val playerName: String,
    val bestTime: Int
)