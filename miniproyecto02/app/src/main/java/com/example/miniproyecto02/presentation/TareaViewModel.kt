package com.example.miniproyecto02.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproyecto02.data.Tarea
import com.example.miniproyecto02.domain.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Filtro(val etiqueta: String) {
    TODAS("Todas"),
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas")
}

enum class Orden(val etiqueta: String) {
    RECIENTES("Más recientes primero"),
    ANTIGUAS("Más antiguas primero"),
    ESTADO("Por estado")
}

class TareaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).tareaDao()

    private val _filtro = MutableStateFlow(Filtro.TODAS)
    val filtro: StateFlow<Filtro> = _filtro.asStateFlow()

    private val _orden = MutableStateFlow(Orden.RECIENTES)
    val orden: StateFlow<Orden> = _orden.asStateFlow()

    // Lista final: datos de Room + filtro + orden elegidos en el menu
    val tareas: StateFlow<List<Tarea>> =
        combine(dao.obtenerTodas(), _filtro, _orden) { lista, filtro, orden ->
            val filtradas = when (filtro) {
                Filtro.TODAS -> lista
                Filtro.PENDIENTES -> lista.filter { !it.completada }
                Filtro.COMPLETADAS -> lista.filter { it.completada }
            }
            when (orden) {
                Orden.RECIENTES -> filtradas.sortedByDescending { it.fechaCreacion }
                Orden.ANTIGUAS -> filtradas.sortedBy { it.fechaCreacion }
                Orden.ESTADO -> filtradas.sortedWith(
                    compareBy<Tarea> { it.completada }.thenByDescending { it.fechaCreacion }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendientes: StateFlow<Int> = dao.contarPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completadas: StateFlow<Int> = dao.contarCompletadas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun cambiarFiltro(filtro: Filtro) { _filtro.value = filtro }

    fun cambiarOrden(orden: Orden) { _orden.value = orden }

    fun agregar(titulo: String, descripcion: String) {
        viewModelScope.launch {
            dao.insertar(Tarea(titulo = titulo, descripcion = descripcion))
        }
    }

    fun editar(tarea: Tarea, titulo: String, descripcion: String) {
        viewModelScope.launch {
            dao.actualizar(tarea.copy(titulo = titulo, descripcion = descripcion))
        }
    }

    fun alternarCompletada(tarea: Tarea) {
        viewModelScope.launch {
            dao.actualizar(tarea.copy(completada = !tarea.completada))
        }
    }

    fun eliminar(tarea: Tarea) {
        viewModelScope.launch { dao.eliminar(tarea) }
    }

    fun eliminarCompletadas() {
        viewModelScope.launch { dao.eliminarCompletadas() }
    }
}
