package com.example.practica08.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    // Read: al devolver un Flow, Compose sabe cuando actualizar la UI
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>

    // Create
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Update
    @Update
    suspend fun actualizar(usuario: Usuario)

    // Delete (reto)
    @Delete
    suspend fun eliminar(usuario: Usuario)
}
