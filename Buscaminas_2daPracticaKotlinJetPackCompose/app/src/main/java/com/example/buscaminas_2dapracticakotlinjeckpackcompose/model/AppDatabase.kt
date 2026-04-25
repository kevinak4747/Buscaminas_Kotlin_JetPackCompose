package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Defino la base de datos de Room
// Aquí indico qué tablas tiene y su versión
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Este metodo devuelve el DAO para poder acceder a los usuarios
    abstract fun userDao(): UserDao

    companion object {

        // Variable para mantener una única instancia de la base de datos
        // Volatile asegura que todos los hilos vean el mismo valor actualizado
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Método para obtener la instancia de la base de datos
        // Si no existe, se crea
        fun getDatabase(context: Context): AppDatabase {

            // Si ya existe, la devuelve directamente
            // Si no, entra en el bloque synchronized para crearla
            return INSTANCE ?: synchronized(this) {

                // Creo la base de datos indicando:
                // - contexto de la aplicación
                // - clase de la base de datos
                // - nombre del archivo en el dispositivo
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "buscaminas_database"
                ).build()

                // Guardo la instancia creada
                INSTANCE = instance

                // Devuelvo la instancia
                instance
            }
        }
    }
}