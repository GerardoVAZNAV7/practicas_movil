package com.example.practica05

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica05.data.PreferencesManager

@Composable
fun FormScreen() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // Estados simples
    var username by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var darkThemeEnabled by remember { mutableStateOf(false) }

    // Carga los datos guardados al abrir la pantalla
    LaunchedEffect(Unit) {
        username = preferencesManager.getUsername()
        notificationsEnabled = preferencesManager.getNotifications()
        darkThemeEnabled = preferencesManager.getDarkTheme()
    }

    // Seleccionamos la paleta de colores según el valor del switch
    val colorScheme = if (darkThemeEnabled) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    // Aplicamos el tema directamente con MaterialTheme
    MaterialTheme(colorScheme = colorScheme) {
        // Surface toma el color de fondo y el color de texto del tema automáticamente
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Práctica 5: Configuración",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineMedium
                )

                HorizontalDivider()

                // 1. Campo de texto para el Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de Usuario") },
                    singleLine = true,
                    isError = username.isBlank(),
                    supportingText = {
                        if (username.isBlank()) {
                            Text("El nombre no puede estar vacío")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Switch para Notificaciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Recibir Notificaciones:", fontSize = 16.sp)
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }

                // 3. Switch para Tema Oscuro (Al moverlo cambia inmediatamente la pantalla)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Activar Tema Oscuro:", fontSize = 16.sp)
                    Switch(
                        checked = darkThemeEnabled,
                        onCheckedChange = { darkThemeEnabled = it }
                    )
                }

                HorizontalDivider()

                // Botón: Guardar en SharedPreferences
                Button(
                    onClick = {
                        // Validamos el caso borde de campo vacío antes de guardar
                        if (username.isBlank()) {
                            Toast.makeText(
                                context,
                                "Escribe un nombre antes de guardar",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            preferencesManager.saveSettings(
                                username = username,
                                notifications = notificationsEnabled,
                                darkTheme = darkThemeEnabled
                            )
                            Toast.makeText(context, "Configuración guardada", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Preferencias")
                }

                // Botón: Recargar/Recuperar (Cargar)
                OutlinedButton(
                    onClick = {
                        if (preferencesManager.hasSavedData()) {
                            username = preferencesManager.getUsername()
                            notificationsEnabled = preferencesManager.getNotifications()
                            darkThemeEnabled = preferencesManager.getDarkTheme()
                            Toast.makeText(context, "Preferencias cargadas", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Aún no hay datos guardados", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Recargar Datos Guardados")
                }

                // Botón: Limpiar / Restablecer
                TextButton(
                    onClick = {
                        preferencesManager.clearPreferences()
                        username = ""
                        notificationsEnabled = false
                        darkThemeEnabled = false
                        Toast.makeText(context, "Preferencias eliminadas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restablecer Configuración", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
