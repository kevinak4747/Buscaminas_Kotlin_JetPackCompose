package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Este DAO define las operaciones que puedo hacer sobre la tabla users
@Dao
interface UserDao {

    // Inserta un usuario nuevo
    // Si ya existe un usuario con el mismo username, Room lo ignora
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    // Busca un usuario por su nombre
    // Devuelve null si no existe
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?
}