package com.example.practica12.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica12.ui.theme.F1DarkGray
import com.example.practica12.ui.theme.F1Gray
import com.example.practica12.ui.theme.F1LightGray
import com.example.practica12.ui.theme.F1Red
import com.example.practica12.ui.theme.F1White

@Composable
fun TablaScreen(viewModel: F1ViewModel, modifier: Modifier = Modifier) {
    Column(modifier) {
        // Selector para cambiar entre la tabla de pilotos y la de constructores
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoTabla.entries.forEach { tipo ->
                FilterChip(
                    selected = viewModel.tipoTabla == tipo,
                    onClick = { viewModel.tipoTabla = tipo },
                    label = { Text(if (tipo == TipoTabla.PILOTOS) "Pilotos" else "Constructores") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = F1DarkGray,
                        labelColor = F1LightGray,
                        selectedContainerColor = F1Red,
                        selectedLabelColor = F1White
                    )
                )
            }
        }

        if (viewModel.tipoTabla == TipoTabla.PILOTOS) {
            when (val estado = viewModel.tablaPilotos) {
                is UiState.Cargando -> PantallaCargando()
                is UiState.Error -> PantallaError(estado.mensaje) { viewModel.cargarTemporada() }
                is UiState.Exito -> {
                    if (estado.datos.isEmpty()) {
                        PantallaVacia("Todavía no hay puntos en la temporada ${viewModel.temporada}")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            items(estado.datos, key = { it.driver?.driverId ?: it.position ?: "" }) { fila ->
                                FilaPosicion(
                                    posicion = fila.position,
                                    titulo = "${fila.driver?.givenName ?: ""} ${fila.driver?.familyName ?: ""}",
                                    subtitulo = fila.constructors?.joinToString(" / ") { it.name ?: "" } ?: "",
                                    puntos = fila.points,
                                    victorias = fila.wins
                                )
                            }
                        }
                    }
                }
            }
        } else {
            when (val estado = viewModel.tablaConstructores) {
                is UiState.Cargando -> PantallaCargando()
                is UiState.Error -> PantallaError(estado.mensaje) { viewModel.cargarTemporada() }
                is UiState.Exito -> {
                    if (estado.datos.isEmpty()) {
                        // El campeonato de constructores empezo hasta 1958
                        PantallaVacia("No hay tabla de constructores para ${viewModel.temporada}")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            items(estado.datos, key = { it.constructor?.constructorId ?: it.position ?: "" }) { fila ->
                                FilaPosicion(
                                    posicion = fila.position,
                                    titulo = fila.constructor?.name ?: "",
                                    subtitulo = fila.constructor?.nationality ?: "",
                                    puntos = fila.points,
                                    victorias = fila.wins
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaPosicion(
    posicion: String?,
    titulo: String,
    subtitulo: String,
    puntos: String?,
    victorias: String?
) {
    val colorPodio = colorPosicion(posicion)
    val podio = esPodio(posicion)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = cardColoresF1(),
        // Borde del color del podio para que se note mas el 1, 2 y 3
        border = if (podio) BorderStroke(2.dp, colorPodio) else null
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(colorPodio),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = posicion ?: "-",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (podio) F1DarkGray else F1White
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(titulo.trim(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitulo, color = F1Gray, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${puntos ?: "0"} pts", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                Text("${victorias ?: "0"} victorias", color = F1LightGray, fontSize = 12.sp)
            }
        }
    }
}
