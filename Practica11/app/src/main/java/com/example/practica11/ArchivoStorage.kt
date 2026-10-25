package com.example.practica11

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/** Lugares donde la app puede guardar el archivo de texto. */
enum class Destino(val etiqueta: String) {
    INTERNO("Memoria interna"),
    EXTERNO_APP("Almacenamiento externo de la app"),
    SDCARD("Tarjeta SD (removible)")
}

/**
 * Encapsula las operaciones de entrada y salida con File, FileOutputStream y FileInputStream.
 * Todas devuelven Result para que la interfaz muestre el éxito o el error sin colapsar.
 */
class ArchivoStorage(private val context: Context) {

    /** Carpeta correspondiente al destino elegido (null si no está disponible). */
    fun directorio(destino: Destino): File? = when (destino) {
        // /data/data/<paquete>/files  (privada, no requiere permisos)
        Destino.INTERNO -> context.filesDir
        // /storage/emulated/0/Android/data/<paquete>/files
        Destino.EXTERNO_APP -> context.getExternalFilesDir(null)
        // /storage/XXXX-XXXX/Android/data/<paquete>/files  (tarjeta SD)
        Destino.SDCARD -> directorioSd()
    }

    /** Busca un volumen externo removible (SDCard) montado. */
    private fun directorioSd(): File? =
        context.getExternalFilesDirs(null)
            .filterNotNull()
            .firstOrNull {
                Environment.isExternalStorageRemovable(it) &&
                    Environment.getExternalStorageState(it) == Environment.MEDIA_MOUNTED
            }

    fun sdDisponible(): Boolean = directorioSd() != null

    /** Escribe el texto. Con agregar = true lo añade al final; con false reemplaza el archivo. */
    fun guardar(destino: Destino, nombre: String, texto: String, agregar: Boolean): Result<File> =
        runCatching {
            val dir = directorio(destino)
                ?: error("${destino.etiqueta} no está disponible en este dispositivo")
            if (!dir.exists()) dir.mkdirs()
            val archivo = File(dir, nombre)
            FileOutputStream(archivo, agregar).use { salida ->
                salida.write(texto.toByteArray(Charsets.UTF_8))
            }
            archivo
        }

    /** Lee todo el contenido del archivo. */
    fun leer(destino: Destino, nombre: String): Result<String> =
        runCatching {
            val dir = directorio(destino)
                ?: error("${destino.etiqueta} no está disponible en este dispositivo")
            val archivo = File(dir, nombre)
            if (!archivo.exists()) error("El archivo $nombre no existe en ${destino.etiqueta}")
            FileInputStream(archivo).use { entrada ->
                entrada.readBytes().toString(Charsets.UTF_8)
            }
        }

    /** Elimina el archivo si existe. */
    fun eliminar(destino: Destino, nombre: String): Result<Boolean> =
        runCatching {
            val dir = directorio(destino)
                ?: error("${destino.etiqueta} no está disponible en este dispositivo")
            File(dir, nombre).delete()
        }
}
