package com.example.miniproyecto02.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    // Read: el Flow avisa a Compose cada vez que cambia la tabla
    @Query("SELECT * FROM tareas")
    fun obtenerTodas(): Flow<List<Tarea>>

    @Query("SELECT COUNT(*) FROM tareas WHERE completada = 0")
    fun contarPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tareas WHERE completada = 1")
    fun contarCompletadas(): Flow<Int>

    // Create
    @Insert
    suspend fun insertar(tarea: Tarea)

    // Update (editar y marcar como completada)
    @Update
    suspend fun actualizar(tarea: Tarea)

    // Delete
    @Delete
    suspend fun eliminar(tarea: Tarea)

    // Accion de la ToolBar: borrar todas las completadas
    @Query("DELETE FROM tareas WHERE completada = 1")
    suspend fun eliminarCompletadas()
}
