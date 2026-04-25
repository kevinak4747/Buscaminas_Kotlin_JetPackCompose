package com.example.buscaminas_2dapracticakotlinjeckpackcompose.model

// Repositorio encargado de trabajar con usuarios
object UserRepository {

    // Creo usuarios iniciales solo si todavía no existen
    suspend fun insertInitialUsers(dao: UserDao) {

        if (dao.getUser("kevin") == null) {
            dao.insert(User("kevin", "1234"))
        }

        if (dao.getUser("pedro") == null) {
            dao.insert(User("pedro", "1234"))
        }
    }

    // Busco un usuario por su nombre
    suspend fun getUser(username: String, dao: UserDao): User? {
        return dao.getUser(username)
    }
}