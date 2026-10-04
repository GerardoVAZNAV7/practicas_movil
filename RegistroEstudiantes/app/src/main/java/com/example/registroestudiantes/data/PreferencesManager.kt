package com.example.registroestudiantes.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Maneja la persistencia local (SharedPreferences) de los estudiantes
 * registrados, tal como se vio en la Práctica 5.
 *
 * Los registros se acumulan en una lista serializada como JSON, de modo que
 * la pantalla de registros pueda mostrar todas las tarjetas guardadas.
 */
class PreferencesManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun guardarRegistro(estudiante: Estudiante) {
        val registros = obtenerRegistros().toMutableList()
        registros.removeAll { it.matricula == estudiante.matricula }
        registros.add(estudiante)
        guardarRegistros(registros)
    }

    fun obtenerRegistros(): List<Estudiante> {
        val raw = prefs.getString(KEY_REGISTROS, null) ?: return emptyList()
        return runCatching {
            val json = JSONArray(raw)
            (0 until json.length()).map { i ->
                val item = json.getJSONObject(i)
                Estudiante(
                    matricula = item.optString("matricula"),
                    nombre = item.optString("nombre"),
                    carrera = item.optString("carrera"),
                    turno = item.optString("turno"),
                    activo = item.optBoolean("activo", true)
                )
            }
        }.getOrDefault(emptyList())
    }

    fun obtenerUltimoRegistro(): Estudiante? = obtenerRegistros().lastOrNull()

    fun borrarRegistros() {
        prefs.edit().remove(KEY_REGISTROS).apply()
    }

    private fun guardarRegistros(registros: List<Estudiante>) {
        val json = JSONArray()
        registros.forEach { estudiante ->
            json.put(
                JSONObject().apply {
                    put("matricula", estudiante.matricula)
                    put("nombre", estudiante.nombre)
                    put("carrera", estudiante.carrera)
                    put("turno", estudiante.turno)
                    put("activo", estudiante.activo)
                }
            )
        }
        prefs.edit().putString(KEY_REGISTROS, json.toString()).apply()
    }

    companion object {
        private const val PREFS_NAME = "registro_estudiantes_prefs"
        private const val KEY_REGISTROS = "registros"
    }
}
