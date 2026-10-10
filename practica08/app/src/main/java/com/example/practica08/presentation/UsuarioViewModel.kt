package com.example.practica08.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica08.data.Usuario
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).usuarioDao()

    // Lista reactiva: se actualiza sola cuando cambia la base de datos
    val usuarios: StateFlow<List<Usuario>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertar(nombre: String) {
        viewModelScope.launch { dao.insertar(Usuario(nombre = nombre)) }
    }

    fun eliminar(usuario: Usuario) {
        viewModelScope.launch { dao.eliminar(usuario) }
    }
}
