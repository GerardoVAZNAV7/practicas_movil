package com.example.practica12.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica12.data.DriverStanding
import com.example.practica12.ui.theme.F1Gray
import com.example.practica12.ui.theme.F1LightGray
import com.example.practica12.ui.theme.F1Red
import com.example.practica12.ui.theme.F1White

// Colores de equipo para la franja de la tarjeta (si no esta el equipo, uso el rojo de F1)
private fun colorEquipo(constructorId: String?): Color = when (constructorId) {
    "red_bull" -> Color(0xFF3671C6)
    "ferrari" -> Color(0xFFE8002D)
    "mercedes" -> Color(0xFF27F4D2)
    "mclaren" -> Color(0xFFFF8000)
    "aston_martin" -> Color(0xFF229971)
    "alpine" -> Color(0xFFFF87BC)
    "williams" -> Color(0xFF64C4FF)
    "rb" -> Color(0xFF6692FF)
    "haas" -> Color(0xFFB6BABD)
    "sauber" -> Color(0xFF52E252)
    else -> F1Red
}

@Composable
fun PilotosScreen(viewModel: F1ViewModel, modifier: Modifier = Modifier) {
    // Las tarjetas salen de la misma tabla de pilotos (ya trae piloto + equipo + puntos)
    when (val estado = viewModel.tablaPilotos) {
        is UiState.Cargando -> PantallaCargando()
        is UiState.Error -> PantallaError(estado.mensaje) { viewModel.cargarTemporada() }
        is UiState.Exito -> {
            if (estado.datos.isEmpty()) {
                PantallaVacia("No hay pilotos registrados para ${viewModel.temporada}")
            } else {
                // Adaptive: en celular salen 2 columnas, en tablet las que quepan
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = modifier,
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(estado.datos, key = { it.driver?.driverId ?: it.position ?: "" }) { piloto ->
                        TarjetaPiloto(piloto)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPiloto(piloto: DriverStanding) {
    val context = LocalContext.current
    val driver = piloto.driver
    val equipo = piloto.constructors?.lastOrNull()
    val colorFranja = colorEquipo(equipo?.constructorId)

    Card(modifier = Modifier.fillMaxWidth(), colors = cardColoresF1()) {
        // Franja de color del equipo
        Box(Modifier.fillMaxWidth().height(6.dp).background(colorFranja))
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top) {
                Text(
                    text = driver?.permanentNumber ?: "-",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorFranja
                )
                Text(driver?.code ?: "", color = F1LightGray, fontWeight = FontWeight.Bold)
            }
            Text(driver?.givenName ?: "", color = F1LightGray, fontSize = 14.sp)
            Text(driver?.familyName?.uppercase() ?: "", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(equipo?.name ?: "Sin equipo", color = F1Gray, fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp))

            Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Nacionalidad: ${driver?.nationality ?: "-"}", fontSize = 12.sp)
                Text("Nacimiento: ${formatearFecha(driver?.dateOfBirth)}", fontSize = 12.sp)
                Text("Posición: ${piloto.position ?: "-"}  ·  ${piloto.points ?: "0"} pts", fontSize = 12.sp)
                Text("Victorias: ${piloto.wins ?: "0"}", fontSize = 12.sp)
            }

            driver?.url?.let { url ->
                TextButton(onClick = { abrirEnlace(context, url) }) {
                    Text("Ver en Wikipedia", color = F1White, fontSize = 13.sp)
                }
            }
        }
    }
}

private fun abrirEnlace(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: ActivityNotFoundException) {
        // si no hay navegador no pasa nada
    }
}
