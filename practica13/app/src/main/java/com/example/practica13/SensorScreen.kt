package com.example.practica13

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

// ---- Parámetros de la detección de sacudida ----
private const val GRAVEDAD = SensorManager.GRAVITY_EARTH // 9.81 m/s²
private const val UMBRAL_SHAKE_G = 2.7f                  // fuerza G necesaria para considerar una sacudida
private const val TIEMPO_ENTRE_SHAKES_MS = 600L          // anti-rebote entre sacudidas
private const val DURACION_ALERTA_MS = 1500L             // cuánto dura la reacción visual

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorScreen(sensorManager: SensorManager) {
    // Estado reactivo: cada cambio recompone la interfaz automáticamente
    var ejeX by remember { mutableFloatStateOf(0f) }
    var ejeY by remember { mutableFloatStateOf(0f) }
    var ejeZ by remember { mutableFloatStateOf(0f) }
    var fuerzaG by remember { mutableFloatStateOf(0f) }
    var sacudidas by remember { mutableIntStateOf(0) }
    var enSacudida by remember { mutableStateOf(false) }
    var disparoShake by remember { mutableLongStateOf(0L) }
    val sensorDisponible = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    }

    // Se enciende el sensor al abrir la pantalla y se apaga al cerrarla (ahorra batería)
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            private var ultimoShake = 0L

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    ejeX = x
                    ejeY = y
                    ejeZ = z

                    // Aceleración vectorial: magnitud del vector (x, y, z) expresada en G.
                    // En reposo vale ~1G (la gravedad).
                    val g = sqrt(x * x + y * y + z * z) / GRAVEDAD
                    fuerzaG = g

                    val ahora = System.currentTimeMillis()
                    if (g > UMBRAL_SHAKE_G && ahora - ultimoShake > TIEMPO_ENTRE_SHAKES_MS) {
                        ultimoShake = ahora
                        sacudidas++
                        disparoShake = ahora // dispara el LaunchedEffect de la reacción visual
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (acelerometro != null) {
            sensorManager.registerListener(
                listener,
                acelerometro,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Reacción visual: activa la alerta y la apaga tras un tiempo
    LaunchedEffect(disparoShake) {
        if (disparoShake != 0L) {
            enSacudida = true
            kotlinx.coroutines.delay(DURACION_ALERTA_MS)
            enSacudida = false
        }
    }

    val colorFondo by animateColorAsState(
        targetValue = if (enSacudida) Color(0xFFD32F2F) else Color(0xFF263238),
        animationSpec = tween(durationMillis = 250),
        label = "colorFondo"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acelerómetro y Shake") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme3Container
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorFondo)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
            ) {
                if (!sensorDisponible) {
                    Text(
                        text = "Este dispositivo no tiene acelerómetro",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Mensaje de reacción a la sacudida
                Text(
                    text = if (enSacudida) "¡SACUDIDA DETECTADA!" else "Sacude el dispositivo",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // Tarjetas con los tres ejes
                TarjetaEje("Eje X", ejeX, Color(0xFFFF8A80))
                TarjetaEje("Eje Y", ejeY, Color(0xFF69F0AE))
                TarjetaEje("Eje Z", ejeZ, Color(0xFF82B1FF))

                Spacer(modifier = Modifier.height(6.dp))

                // Fuerza vectorial y barra visual contra el umbral
                Text(
                    text = "Fuerza total: ${"%.2f".format(fuerzaG)} G  (umbral: $UMBRAL_SHAKE_G G)",
                    color = Color.White,
                    fontSize = 16.sp
                )
                LinearProgressIndicator(
                    progress = { (fuerzaG / (UMBRAL_SHAKE_G * 1.5f)).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = if (fuerzaG > UMBRAL_SHAKE_G) Color.Yellow else Color(0xFF4CAF50),
                    trackColor = Color(0x55FFFFFF)
                )

                Text(
                    text = "Sacudidas detectadas: $sacudidas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TarjetaEje(nombre: String, valor: Float, colorAcento: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF37474F))
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(text = nombre, color = colorAcento, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "${"%.2f".format(valor)} m/s²",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private val MaterialTheme3Container = Color(0xFFE8DEF8)
