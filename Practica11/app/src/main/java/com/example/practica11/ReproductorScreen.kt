package com.example.practica11

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Mini reproductor MP3 de la práctica 11.
 * Usa el Storage Access Framework (GetContent) para elegir un audio de la memoria o la SD
 * sin pedir permisos, y MediaPlayer para reproducirlo.
 */
@Composable
fun ReproductorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Estados para la URI del archivo, el reproductor y si está sonando
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Lanzador para abrir el explorador de archivos
    val selectorAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            audioUri = uri

            // Liberar el reproductor anterior si existía para evitar fugas de memoria
            mediaPlayer?.release()

            // Crear un nuevo MediaPlayer con el archivo seleccionado
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer?.start()
            isPlaying = mediaPlayer != null

            // Escuchar cuando la canción termine automáticamente
            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
            }
        }
    }

    // DisposableEffect limpia los recursos del MediaPlayer si el usuario sale de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (audioUri == null) "Ningún archivo seleccionado" else "Audio cargado correctamente",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para abrir el explorador y filtrado de archivos "solo audios"
        Button(onClick = { selectorAudio.launch("audio/*") }) {
            Text("Buscar MP3 en SD/Memoria")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Controles de reproducción
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Reproducir
            FloatingActionButton(onClick = {
                if (mediaPlayer != null && !isPlaying) {
                    mediaPlayer?.start()
                    isPlaying = true
                }
            }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
            }

            // Pausar
            FloatingActionButton(onClick = {
                if (mediaPlayer != null && isPlaying) {
                    mediaPlayer?.pause()
                    isPlaying = false
                }
            }) {
                Icon(Icons.Default.Pause, contentDescription = "Pausar")
            }

            // RETO: Detener = pausar + regresar la pista al segundo cero
            FloatingActionButton(onClick = {
                if (mediaPlayer != null) {
                    mediaPlayer?.pause()
                    isPlaying = false
                    mediaPlayer?.seekTo(0)
                }
            }) {
                Icon(Icons.Default.Stop, contentDescription = "Detener")
            }
        }
    }
}
