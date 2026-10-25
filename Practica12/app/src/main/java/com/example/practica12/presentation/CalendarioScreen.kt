package com.example.practica12.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica12.data.Race
import com.example.practica12.data.Session
import com.example.practica12.ui.theme.F1Gray
import com.example.practica12.ui.theme.F1LightGray
import com.example.practica12.ui.theme.F1Red
import com.example.practica12.ui.theme.F1White

@Composable
fun CalendarioScreen(viewModel: F1ViewModel, modifier: Modifier = Modifier) {
    when (val estado = viewModel.calendario) {
        is UiState.Cargando -> PantallaCargando()
        is UiState.Error -> PantallaError(estado.mensaje) { viewModel.cargarTemporada() }
        is UiState.Exito -> {
            if (estado.datos.isEmpty()) {
                PantallaVacia("No hay calendario disponible para ${viewModel.temporada}")
            } else {
                // La proxima carrera es la primera cuyo dia es hoy o despues (solo en la temporada actual)
                val hoy = fechaDeHoy()
                val proxima = if (viewModel.temporada == viewModel.anioActual)
                    estado.datos.firstOrNull { (it.date ?: "") >= hoy }?.round else null

                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
                ) {
                    items(estado.datos, key = { "${it.season}-${it.round}" }) { carrera ->
                        CarreraCard(
                            carrera = carrera,
                            esProxima = carrera.round == proxima,
                            yaPaso = (carrera.date ?: "9999") < hoy
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CarreraCard(carrera: Race, esProxima: Boolean, yaPaso: Boolean) {
    var expandido by rememberSaveable(carrera.season, carrera.round) { mutableStateOf(false) }
    val hora = horaLocal(carrera.date, carrera.time)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expandido = !expandido },
        colors = cardColoresF1(),
        border = if (esProxima) BorderStroke(2.dp, F1Red) else null
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circulo rojo con el numero de ronda (gris si la carrera ya paso)
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(if (yaPaso) F1Gray else F1Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(carrera.round ?: "-", color = F1White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    if (esProxima) {
                        Text("PRÓXIMA CARRERA", color = F1Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(carrera.raceName ?: "", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(carrera.circuit?.circuitName ?: "", color = F1LightGray, fontSize = 13.sp)
                    val lugar = listOfNotNull(carrera.circuit?.location?.locality, carrera.circuit?.location?.country)
                        .joinToString(", ")
                    Text(lugar, color = F1Gray, fontSize = 13.sp)
                }
            }
            Text(
                text = formatearFecha(carrera.date) + (hora?.let { "  ·  $it h" } ?: ""),
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Medium
            )

            // Al tocar la tarjeta se ven todas las sesiones del fin de semana
            if (expandido) {
                val sesiones = listOf(
                    "Práctica 1" to carrera.firstPractice,
                    "Práctica 2" to carrera.secondPractice,
                    "Práctica 3" to carrera.thirdPractice,
                    "Clasificación sprint" to carrera.sprintQualifying,
                    "Sprint" to carrera.sprint,
                    "Clasificación" to carrera.qualifying,
                    "Carrera" to Session(carrera.date, carrera.time)
                ).filter { it.second?.date != null }

                Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    sesiones.forEach { (nombre, sesion) ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(nombre, color = F1LightGray, fontSize = 13.sp)
                            Text(
                                formatearFecha(sesion?.date) +
                                    (horaLocal(sesion?.date, sesion?.time)?.let { "  $it h" } ?: ""),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
