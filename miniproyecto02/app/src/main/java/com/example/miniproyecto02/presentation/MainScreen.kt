package com.example.miniproyecto02.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miniproyecto02.data.Tarea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TareaViewModel = viewModel()) {

    val tareas by viewModel.tareas.collectAsState()
    val filtro by viewModel.filtro.collectAsState()
    val orden by viewModel.orden.collectAsState()
    val pendientes by viewModel.pendientes.collectAsState()
    val completadas by viewModel.completadas.collectAsState()

    var menuAbierto by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tareaEnEdicion by remember { mutableStateOf<Tarea?>(null) }
    var confirmarBorrado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mis Tareas", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$pendientes pendientes · $completadas completadas",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Accion directa: borrar completadas
                    IconButton(
                        onClick = { confirmarBorrado = true },
                        enabled = completadas > 0
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar completadas")
                    }

                    // Menu desplegable: filtrar y ordenar
                    Box {
                        IconButton(onClick = { menuAbierto = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                        }
                        DropdownMenu(
                            expanded = menuAbierto,
                            onDismissRequest = { menuAbierto = false }
                        ) {
                            Text(
                                "Filtrar por",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Filtro.entries.forEach { f ->
                                DropdownMenuItem(
                                    text = { Text(f.etiqueta) },
                                    leadingIcon = {
                                        if (f == filtro) Icon(Icons.Default.Check, contentDescription = null)
                                        else Spacer(Modifier.size(24.dp))
                                    },
                                    onClick = {
                                        viewModel.cambiarFiltro(f)
                                        menuAbierto = false
                                    }
                                )
                            }

                            HorizontalDivider()

                            Text(
                                "Ordenar por",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            Orden.entries.forEach { o ->
                                DropdownMenuItem(
                                    text = { Text(o.etiqueta) },
                                    leadingIcon = {
                                        if (o == orden) Icon(Icons.Default.Check, contentDescription = null)
                                        else Spacer(Modifier.size(24.dp))
                                    },
                                    onClick = {
                                        viewModel.cambiarOrden(o)
                                        menuAbierto = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                tareaEnEdicion = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nueva tarea")
            }
        }
    ) { padding ->

        if (tareas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay tareas para mostrar.\nToca + para agregar una.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tareas, key = { it.id }) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onToggle = { viewModel.alternarCompletada(tarea) },
                        onEditar = {
                            tareaEnEdicion = tarea
                            mostrarDialogo = true
                        },
                        onEliminar = { viewModel.eliminar(tarea) }
                    )
                }
            }
        }
    }

    // Dialogo para crear o editar
    if (mostrarDialogo) {
        TareaDialog(
            tareaInicial = tareaEnEdicion,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { titulo, descripcion ->
                val actual = tareaEnEdicion
                if (actual == null) viewModel.agregar(titulo, descripcion)
                else viewModel.editar(actual, titulo, descripcion)
                mostrarDialogo = false
            }
        )
    }

    // Confirmacion para borrar completadas
    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("Borrar completadas") },
            text = { Text("Se eliminarán $completadas tareas completadas. ¿Continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarCompletadas()
                    confirmarBorrado = false
                }) { Text("Borrar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun TareaItem(
    tarea: Tarea,
    onToggle: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX")) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = { onToggle() })

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                )
                if (tarea.descripcion.isNotBlank()) {
                    Text(
                        text = tarea.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = formato.format(Date(tarea.fechaCreacion)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar tarea")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
            }
        }
    }
}

@Composable
private fun TareaDialog(
    tareaInicial: Tarea?,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    var titulo by remember { mutableStateOf(tareaInicial?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tareaInicial?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (tareaInicial == null) "Nueva tarea" else "Editar tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onGuardar(titulo.trim(), descripcion.trim()) },
                enabled = titulo.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
