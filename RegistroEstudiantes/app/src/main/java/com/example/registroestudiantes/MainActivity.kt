package com.example.registroestudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.registroestudiantes.ui.ConfirmacionScreen
import com.example.registroestudiantes.ui.FormularioScreen
import com.example.registroestudiantes.ui.RegistrosScreen
import com.example.registroestudiantes.ui.theme.RegistroEstudiantesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroEstudiantesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "formulario") {
                        composable("formulario") {
                            FormularioScreen(navController = navController)
                        }
                        composable(
                            route = "confirmacion/{matricula}/{nombre}/{carrera}/{turno}/{activo}",
                            arguments = listOf(
                                navArgument("matricula") { type = NavType.StringType },
                                navArgument("nombre") { type = NavType.StringType },
                                navArgument("carrera") { type = NavType.StringType },
                                navArgument("turno") { type = NavType.StringType },
                                navArgument("activo") { type = NavType.BoolType }
                            )
                        ) { backStackEntry ->
                            ConfirmacionScreen(
                                navController = navController,
                                matricula = backStackEntry.arguments?.getString("matricula") ?: "",
                                nombre = backStackEntry.arguments?.getString("nombre") ?: "",
                                carrera = backStackEntry.arguments?.getString("carrera") ?: "",
                                turno = backStackEntry.arguments?.getString("turno") ?: "",
                                activo = backStackEntry.arguments?.getBoolean("activo") ?: true
                            )
                        }
                        composable("registros") {
                            RegistrosScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
