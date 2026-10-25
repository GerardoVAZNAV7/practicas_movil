Práctica 11: Manejo de archivos y SDCard (Android 14)

Aplicación en Kotlin y Jetpack Compose con dos pantallas:

1. Archivos: guarda, agrega, lee y elimina un archivo de texto en memoria interna,
   almacenamiento externo de la app o tarjeta SD, usando File, FileOutputStream y FileInputStream.
   Incluye los permisos READ_EXTERNAL_STORAGE y WRITE_EXTERNAL_STORAGE en el manifiesto y su
   solicitud en tiempo de ejecución (en Android 10 o inferior; desde Android 11 ya no aplican).
2. Reproductor MP3: practica 11 del PDF con Storage Access Framework y MediaPlayer, más el
   botón Detener del reto.

Requisitos: Android Studio Koala o superior, JDK 17, compileSdk y targetSdk 34.

Cómo abrir: File, Open, selecciona la carpeta Practica11 y espera la sincronización de Gradle.

Cómo probar la SD: en el Device Manager edita el emulador, Show Advanced Settings, y asigna
un tamaño en SD Card. Para el MP3, arrastra un archivo de audio al emulador.
