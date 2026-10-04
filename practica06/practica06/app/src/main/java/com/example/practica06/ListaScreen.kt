package com.example.practica06

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListaScreen() {
    // Obtenemos la lista de datos desde nuestra función
    val contactos = obtenerContactosDummy()

    // LazyColumn es el equivalente directo a RecyclerView en Compose
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado automático entre elementos
    ) {
        // items() itera sobre la colección; key ayuda a Compose a identificar cada fila
        items(contactos, key = { it.id }) { contacto ->
            ContactoItem(contacto = contacto)
        }
    }
}

@Composable
fun ContactoItem(contacto: Contacto) {
    // Contexto local (se declara al inicio del Composable, no dentro de los parámetros)
    val context = LocalContext.current

    // Card para mostrar los datos de cada elemento de la lista
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // RETO: la tarjeta responde al clic y muestra un Toast con el nombre
            .clickable {
                Toast.makeText(
                    context,
                    "Seleccionaste a ${contacto.nombre}",
                    Toast.LENGTH_SHORT
                ).show()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3F2FD))
                .padding(16.dp)
        ) {
            Text(
                text = contacto.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tel: ${contacto.telefono}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}
