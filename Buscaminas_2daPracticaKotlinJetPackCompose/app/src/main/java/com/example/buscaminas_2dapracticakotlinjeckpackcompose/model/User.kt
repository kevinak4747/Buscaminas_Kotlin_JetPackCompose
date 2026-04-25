package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa la tabla de usuarios en la base de datos
@Entity(tableName = "users")
data class User(

    // Clave primaria, no puede repetirse
    @PrimaryKey
    val username: String,

    // Contraseña del usuario
    val password: String
)