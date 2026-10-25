package com.example.practica12.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica12.data.ConstructorStanding
import com.example.practica12.data.DriverStanding
import com.example.practica12.data.F1Repository
import com.example.practica12.data.Race
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar

// Los tres estados que puede tener cada pantalla
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Error(val mensaje: String) : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
}

enum class TipoTabla { PILOTOS, CONSTRUCTORES }

class F1ViewModel : ViewModel() {
    private val repository = F1Repository()

    val anioActual: Int = Calendar.getInstance().get(Calendar.YEAR)

    var temporada by mutableIntStateOf(anioActual)
        private set
    var tipoTabla by mutableStateOf(TipoTabla.PILOTOS)

    var calendario by mutableStateOf<UiState<List<Race>>>(UiState.Cargando)
        private set
    var tablaPilotos by mutableStateOf<UiState<List<DriverStanding>>>(UiState.Cargando)
        private set
    var tablaConstructores by mutableStateOf<UiState<List<ConstructorStanding>>>(UiState.Cargando)
        private set

    private var trabajo: Job? = null

    init {
        cargarTemporada()
    }

    fun cambiarTemporada(anio: Int) {
        if (anio != temporada) {
            temporada = anio
            cargarTemporada()
        }
    }

    // IMPORTANTE: las 3 peticiones van una por una (no en paralelo) porque Jolpica limita
    // a ~4 peticiones por segundo y me mandaba error 429. Todo corre en corrutina,
    // asi la interfaz nunca se congela mientras espera internet.
    // Si cambio de temporada a la mitad se cancela el trabajo anterior.
    fun cargarTemporada() {
        trabajo?.cancel()
        calendario = UiState.Cargando
        tablaPilotos = UiState.Cargando
        tablaConstructores = UiState.Cargando
        trabajo = viewModelScope.launch {
            calendario = ejecutar { repository.getCalendario(temporada) }
            tablaPilotos = ejecutar { repository.getTablaPilotos(temporada) }
            tablaConstructores = ejecutar { repository.getTablaConstructores(temporada) }
        }
    }

    private suspend fun <T> ejecutar(bloque: suspend () -> T): UiState<T> =
        try {
            UiState.Exito(bloque())
        } catch (e: CancellationException) {
            throw e // no tragarme la cancelacion, si no se rompe el cambio de temporada
        } catch (e: HttpException) {
            if (e.code() == 429) UiState.Error("Demasiadas peticiones, espera unos segundos e intenta de nuevo")
            else UiState.Error("Error del servidor (${e.code()})")
        } catch (e: IOException) {
            UiState.Error("Sin conexión a internet")
        } catch (e: Exception) {
            UiState.Error("Algo salió mal: ${e.message}")
        }
}
