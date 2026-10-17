package com.example.practica09

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

// IDs de imágenes de prueba (Picsum). La URL se arma según el tamaño que se necesite.
private val imageIds = listOf(
    1018, 1015, 1019, 1020, 1021, 1022,
    1023, 1024, 1025, 1026, 1027, 1028
)

private fun urlMiniatura(id: Int) = "https://picsum.photos/id/$id/400/400"
private fun urlGrande(id: Int) = "https://picsum.photos/id/$id/1000/1000"

@Composable
fun GaleriaScreen() {
    // Estado: id de la imagen seleccionada (null = ningún diálogo abierto).
    // rememberSaveable lo conserva al girar la pantalla.
    var imagenSeleccionada by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Mi Galería",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )

            LazyVerticalGrid(
                // Adaptive: las columnas se ajustan al ancho de la pantalla
                columns = GridCells.Adaptive(minSize = 150.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(imageIds) { id ->
                    ImagenItem(
                        url = urlMiniatura(id),
                        onClick = { imagenSeleccionada = id }
                    )
                }
            }
        }
    }

    // Vista de detalle: se muestra solo cuando hay una imagen seleccionada
    imagenSeleccionada?.let { id ->
        DetalleImagenDialog(
            url = urlGrande(id),
            onDismiss = { imagenSeleccionada = null }
        )
    }
}

@Composable
fun ImagenItem(url: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick), // Reto: imagen interactiva
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        ImagenAsincrona(
            url = url,
            descripcion = "Imagen de la galería",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DetalleImagenDialog(url: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        // false = el diálogo puede ocupar casi todo el ancho de la pantalla
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ImagenAsincrona(
                    url = url,
                    descripcion = "Imagen ampliada",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onDismiss) // tocar la imagen también cierra
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
            }
        }
    }
}

/**
 * Imagen cargada con Coil, con indicador de carga y mensaje de error.
 * Coil descarga en segundo plano y guarda en caché, así no se bloquea la UI.
 */
@Composable
fun ImagenAsincrona(url: String, descripcion: String, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = descripcion,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        loading = {
            // Placeholder mientras se descarga
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        },
        error = {
            // Se muestra si falla la descarga (sin internet, URL mala, etc.)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No se pudo cargar",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}
