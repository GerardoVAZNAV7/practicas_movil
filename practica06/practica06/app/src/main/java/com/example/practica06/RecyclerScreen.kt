package com.example.practica06

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Versión clásica con RecyclerView (vistas XML) incrustada dentro de Compose
@Composable
fun RecyclerScreen() {
    val contactos = remember { obtenerContactosDummy() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            RecyclerView(context).apply {
                // LayoutManager: define la disposición (lista vertical).
                // Para cuadrícula: GridLayoutManager(context, 2)
                layoutManager = LinearLayoutManager(context)
                adapter = ContactoAdapter(contactos) { contacto ->
                    Toast.makeText(
                        context,
                        "Seleccionaste a ${contacto.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )
}
