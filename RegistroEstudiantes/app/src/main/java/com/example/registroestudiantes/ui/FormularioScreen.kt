package com.example.registroestudiantes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.registroestudiantes.R
import com.example.registroestudiantes.data.Estudiante
import com.example.registroestudiantes.data.PreferencesManager

private val carreras = listOf(
    "Ingeniería en Software",
    "Ingeniería Industrial",
    "Ingeniería Civil",
    "Ingeniería Mecatrónica"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(navController: NavHostController) {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    val ultimo = remember { prefs.obtenerUltimoRegistro() }

    // Se recuerda el último valor guardado, como en la Práctica 5
    var matricula by remember { mutableStateOf(ultimo?.matricula ?: "") }
    var nombre by remember { mutableStateOf(ultimo?.nombre ?: "") }
    var carrera by remember { mutableStateOf(ultimo?.carrera?.ifBlank { carreras[0] } ?: carreras[0]) }
    var turno by remember { mutableStateOf(ultimo?.turno ?: "Matutino") }
    var activo by remember { mutableStateOf(ultimo?.activo ?: true) }
    var expandedCarrera by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.uas_logo),
            contentDescription = "Logo UAS",
            modifier = Modifier
                .size(96.dp)
                .padding(bottom = 12.dp)
        )

        Text(
            text = "Sistema de Registro de Estudiantes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Carrera - Exposed Dropdown Menu
        ExposedDropdownMenuBox(
            expanded = expandedCarrera,
            onExpandedChange = { expandedCarrera = !expandedCarrera }
        ) {
            OutlinedTextField(
                value = carrera,
                onValueChange = {},
                readOnly = true,
                label = { Text("Carrera") },
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCarrera,
                onDismissRequest = { expandedCarrera = false }
            ) {
                carreras.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            carrera = opcion
                            expandedCarrera = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Turno - RadioButton
        Text("Turno", fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = turno == "Matutino", onClick = { turno = "Matutino" })
            Text("Matutino", modifier = Modifier.padding(end = 16.dp))
            RadioButton(selected = turno == "Vespertino", onClick = { turno = "Vespertino" })
            Text("Vespertino")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Estatus - Switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Estatus: ${if (activo) "Activo" else "Inactivo"}", modifier = Modifier.weight(1f))
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                // Persistencia local (SharedPreferences)
                prefs.guardarRegistro(
                    Estudiante(
                        matricula = matricula,
                        nombre = nombre,
                        carrera = carrera,
                        turno = turno,
                        activo = activo
                    )
                )

                // Navegación pasando los datos como parámetros de ruta
                navController.navigate(
                    "confirmacion/$matricula/${nombre.ifBlank { "-" }}/${carrera}/$turno/$activo"
                )
            },
            enabled = matricula.isNotBlank() && nombre.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate("registros") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver registros guardados")
        }
    }
}
