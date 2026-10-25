package com.example.practica12.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.practica12.ui.theme.F1DarkGray
import com.example.practica12.ui.theme.F1Red
import com.example.practica12.ui.theme.F1White
import com.example.practica12.ui.theme.PodioBronce
import com.example.practica12.ui.theme.PodioOro
import com.example.practica12.ui.theme.PodioPlata
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun cardColoresF1() = CardDefaults.cardColors(containerColor = F1DarkGray, contentColor = F1White)

/** Color de la posicion: 1 oro, 2 plata, 3 bronce y el resto gris oscuro (tipo podio). */
fun colorPosicion(posicion: String?): Color = when (posicion?.toIntOrNull()) {
    1 -> PodioOro
    2 -> PodioPlata
    3 -> PodioBronce
    else -> Color(0xFF15151E)
}

fun esPodio(posicion: String?): Boolean = (posicion?.toIntOrNull() ?: 99) <= 3

@Composable
fun PantallaCargando() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = F1Red)
    }
}

@Composable
fun PantallaError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(mensaje, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = onReintentar,
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = F1Red)
        ) { Text("Reintentar") }
    }
}

@Composable
fun PantallaVacia(mensaje: String) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(mensaje, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
    }
}

// ---------- Formato de fechas ----------
private val locale = Locale("es", "MX")

fun formatearFecha(fecha: String?): String {
    if (fecha == null) return ""
    return try {
        val d = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(fecha)
        SimpleDateFormat("d MMM yyyy", locale).format(d!!)
    } catch (e: Exception) {
        fecha
    }
}

// La API da la hora en UTC (termina con Z), la paso a la hora del telefono
fun horaLocal(fecha: String?, hora: String?): String? {
    if (fecha == null || hora == null) return null
    return try {
        val entrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        entrada.timeZone = TimeZone.getTimeZone("UTC")
        val d: Date = entrada.parse("${fecha}T$hora")!!
        SimpleDateFormat("HH:mm", locale).format(d)
    } catch (e: Exception) {
        null
    }
}

fun fechaDeHoy(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
