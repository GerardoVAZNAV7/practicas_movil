package com.example.registroestudiantes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = UasAzul,
    secondary = UasDorado,
    background = UasGrisClaro,
    surface = UasBlanco
)

private val DarkColors = darkColorScheme(
    primary = UasDorado,
    secondary = UasAzul
)

@Composable
fun RegistroEstudiantesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
