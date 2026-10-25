package com.example.practica12.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta oficial de la F1
val F1Red = Color(0xFFE10600)
val F1Black = Color(0xFF15151E)
val F1DarkGray = Color(0xFF38383F)
val F1Gray = Color(0xFF949498)
val F1LightGray = Color(0xFFD0D0D2)
val F1White = Color(0xFFFFFFFF)

// Colores del podio (1, 2 y 3)
val PodioOro = Color(0xFFFFD700)
val PodioPlata = Color(0xFFC0C0C0)
val PodioBronce = Color(0xFFCD7F32)

private val F1Colors = darkColorScheme(
    primary = F1Red,
    onPrimary = F1White,
    secondary = F1Gray,
    background = F1Black,
    onBackground = F1White,
    surface = F1Black,
    onSurface = F1White,
    surfaceVariant = F1DarkGray,
    onSurfaceVariant = F1LightGray
)

@Composable
fun F1Theme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = F1Colors, content = content)
}
