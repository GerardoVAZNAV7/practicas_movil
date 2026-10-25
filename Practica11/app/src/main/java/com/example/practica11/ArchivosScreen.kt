package com.example.practica11

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

// Permisos de almacenamiento que pide la práctica (declarados también en el manifiesto).
private val PERMISOS = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

/**
 * Desde Android 11 (API 30) el sistema ya no concede estos permisos para archivos propios
 * (scoped storage), así que solo se piden en Android 10 o inferior.
 */
private fun requierePermisosEnTiempoDeEjecucion() =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.R

private fun permisosConcedidos(context: Context): Boolean {
    if (!requierePermisosEnTiempoDeEjecucion()) return true
    return PERMISOS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

/** Deja el nombre seguro (sin rutas ni símbolos) y con extensión .txt. */
private fun normalizarNombre(nombre: String): String {
    val limpio = nombre.trim().replace(Regex("[^A-Za-z0-9_.]"), "_").ifEmpty { "nota" }
    return if (limpio.endsWith(".txt")) limpio else "$limpio.txt"
}

@Composable
fun ArchivosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val almacenamiento = remember { ArchivoStorage(context) }

    var nombre by rememberSaveable { mutableStateOf("nota.txt") }
    var contenido by rememberSaveable { mutableStateOf("") }
    var destino by rememberSaveable { mutableStateOf(Destino.INTERNO) }
    var resultado by rememberSaveable { mutableStateOf("Elige un destino y presiona un botón.") }
    var textoLeido by rememberSaveable { mutableStateOf("") }
    var permisosOk by remember { mutableStateOf(permisosConcedidos(context)) }
    var accionPendiente by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Solicitud de permisos en tiempo de ejecución (runtime permissions)
    val lanzadorPermisos = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { respuesta ->
        permisosOk = respuesta.values.all { it }
        if (permisosOk) {
            accionPendiente?.invoke()
        } else {
            resultado = "Permiso de almacenamiento denegado. No se puede usar el almacenamiento externo."
        }
        accionPendiente = null
    }

    /** Ejecuta la acción; si el destino es externo y faltan permisos, los pide primero. */
    fun conPermisos(accion: () -> Unit) {
        val esExterno = destino != Destino.INTERNO
        if (esExterno && !permisosConcedidos(context)) {
            accionPendiente = accion
            lanzadorPermisos.launch(PERMISOS)
        } else {
            accion()
        }
    }

    fun guardar(agregar: Boolean) = conPermisos {
        almacenamiento.guardar(destino, normalizarNombre(nombre), contenido, agregar)
            .onSuccess {
                resultado = (if (agregar) "Texto agregado en: " else "Archivo guardado en: ") +
                    it.absolutePath
            }
            .onFailure { resultado = "Error al guardar: ${it.message}" }
    }

    fun leer() = conPermisos {
        almacenamiento.leer(destino, normalizarNombre(nombre))
            .onSuccess {
                textoLeido = it
                resultado = "Archivo leído correctamente (${it.length} caracteres)."
            }
            .onFailure {
                textoLeido = ""
                resultado = "Error al leer: ${it.message}"
            }
    }

    fun eliminar() = conPermisos {
        almacenamiento.eliminar(destino, normalizarNombre(nombre))
            .onSuccess {
                textoLeido = ""
                resultado = if (it) "Archivo eliminado." else "No había un archivo con ese nombre."
            }
            .onFailure { resultado = "Error al eliminar: ${it.message}" }
    }

    val estadoPermisos = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}): " +
        if (requierePermisosEnTiempoDeEjecucion()) {
            "READ y WRITE_EXTERNAL_STORAGE se piden en tiempo de ejecución. " +
                if (permisosOk) "Estado: concedidos." else "Estado: pendientes."
        } else {
            "READ y WRITE_EXTERNAL_STORAGE ya no aplican (scoped storage). " +
                "Las carpetas propias de la app no necesitan permisos."
        }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = estadoPermisos,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del archivo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Texto a guardar") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Destino", style = MaterialTheme.typography.titleSmall)
        Destino.entries.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = destino == opcion,
                        onClick = { destino = opcion },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = destino == opcion, onClick = null)
                val aviso =
                    if (opcion == Destino.SDCARD && !almacenamiento.sdDisponible()) " (no detectada)" else ""
                Text(text = opcion.etiqueta + aviso, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { guardar(agregar = false) }, modifier = Modifier.weight(1f)) {
                Text("Guardar")
            }
            OutlinedButton(onClick = { guardar(agregar = true) }, modifier = Modifier.weight(1f)) {
                Text("Agregar")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { leer() }, modifier = Modifier.weight(1f)) {
                Text("Leer")
            }
            OutlinedButton(onClick = { eliminar() }, modifier = Modifier.weight(1f)) {
                Text("Eliminar")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Resultado", style = MaterialTheme.typography.titleSmall)
                Text(resultado, style = MaterialTheme.typography.bodyMedium)
                if (textoLeido.isNotEmpty()) {
                    Text(
                        "Contenido leído",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(textoLeido, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
