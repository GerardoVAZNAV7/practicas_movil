package com.example.practica12

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.practica12.presentation.CalendarioScreen
import com.example.practica12.presentation.F1ViewModel
import com.example.practica12.presentation.PilotosScreen
import com.example.practica12.presentation.TablaScreen
import com.example.practica12.ui.theme.F1Black
import com.example.practica12.ui.theme.F1Gray
import com.example.practica12.ui.theme.F1Red
import com.example.practica12.ui.theme.F1Theme
import com.example.practica12.ui.theme.F1White

class MainActivity : ComponentActivity() {
    // El ViewModel se crea aqui para que sobreviva a los giros de pantalla
    private val viewModel: F1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras del sistema oscuras para que los iconos se vean sobre el fondo negro de F1
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent {
            F1Theme {
                F1App(viewModel)
            }
        }
    }
}

private data class Destino(val ruta: String, val etiqueta: String, val icono: ImageVector)

// El calendario va al CENTRO (es la pantalla principal)
private val destinos = listOf(
    Destino("tabla", "Puntos", Icons.Filled.EmojiEvents),
    Destino("calendario", "Calendario", Icons.Filled.CalendarMonth),
    Destino("pilotos", "Pilotos", Icons.Filled.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun F1App(viewModel: F1ViewModel) {
    val navController = rememberNavController()
    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = F1Black,
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.f1_logo),
                        contentDescription = "Logo F1",
                        modifier = Modifier.height(26.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                actions = {
                    SelectorTemporada(
                        actual = viewModel.temporada,
                        maxima = viewModel.anioActual,
                        onSeleccion = { viewModel.cambiarTemporada(it) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = F1Black)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = F1Black) {
                destinos.forEach { destino ->
                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = {
                            navController.navigate(destino.ruta) {
                                // Para que no se apilen pantallas cada vez que toco la barra
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                        label = { Text(destino.etiqueta) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = F1White,
                            selectedTextColor = F1White,
                            indicatorColor = F1Red,
                            unselectedIconColor = F1Gray,
                            unselectedTextColor = F1Gray
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "calendario", // pantalla por defecto
            modifier = Modifier.padding(padding)
        ) {
            composable("tabla") { TablaScreen(viewModel) }
            composable("calendario") { CalendarioScreen(viewModel) }
            composable("pilotos") { PilotosScreen(viewModel) }
        }
    }
}

// Menu para cambiar de temporada, de la actual hasta 1950 (primer campeonato de F1)
@Composable
private fun SelectorTemporada(actual: Int, maxima: Int, onSeleccion: (Int) -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { abierto = true }) {
            Text("Temporada $actual", color = F1White, fontWeight = FontWeight.Bold)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Cambiar temporada", tint = F1White)
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            (maxima downTo 1950).forEach { anio ->
                DropdownMenuItem(
                    text = { Text("$anio") },
                    onClick = {
                        abierto = false
                        onSeleccion(anio)
                    }
                )
            }
        }
    }
}
