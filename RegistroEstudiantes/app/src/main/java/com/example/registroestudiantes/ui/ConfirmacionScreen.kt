package com.example.registroestudiantes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.registroestudiantes.R

@Composable
fun ConfirmacionScreen(
    navController: NavHostController,
    matricula: String,
    nombre: String,
    carrera: String,
    turno: String,
    activo: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.uas_logo),
            contentDescription = "Logo UAS",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Registro exitoso",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                DatoFila("Matrícula", matricula)
                DatoFila("Nombre", nombre)
                DatoFila("Carrera", carrera)
                DatoFila("Turno", turno)
                DatoFila("Estatus", if (activo) "Activo" else "Inactivo")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al formulario")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate("registros") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver registros guardados")
        }
    }
}

@Composable
private fun DatoFila(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "$etiqueta: ", fontWeight = FontWeight.Bold)
        Text(text = valor)
    }
}
