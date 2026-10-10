package com.example.miniproyecto02.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Morado = Color(0xFF6750A4)

private val EsquemaClaro = lightColorScheme(
    primary = Morado,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D)
)

@Composable
fun Miniproyecto02Theme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro) {
        Surface(color = MaterialTheme.colorScheme.background, content = content)
    }
}
