package com.example.registroestudiantes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.registroestudiantes.data.Estudiante
import com.example.registroestudiantes.data.PreferencesManager

@Composable
fun RegistrosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    val registros = prefs.obtenerRegistros()
    val volverAlFormulario: () -> Unit = {
        navController.popBackStack("formulario", inclusive = false)
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = volverAlFormulario) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver al formulario"
                )
            }
            Text(
                text = "Registros guardados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (registros.isEmpty()) {
                "Sin registros"
            } else {
                "Total de estudiantes: ${registros.size}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (registros.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Aún no hay estudiantes registrados.\nUsa el formulario para capturar el primero.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(registros) { estudiante ->
                    EstudianteCard(estudiante)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = volverAlFormulario,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al formulario")
        }
    }
}

@Composable
private fun EstudianteCard(estudiante: Estudiante) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = estudiante.nombre,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = { },
                    label = { Text(if (estudiante.activo) "Activo" else "Inactivo") }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Matrícula: ${estudiante.matricula}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Carrera: ${estudiante.carrera}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Turno: ${estudiante.turno}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
