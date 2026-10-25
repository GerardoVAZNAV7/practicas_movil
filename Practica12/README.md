Práctica 12: Consumo de API REST (Fórmula 1) para Android 14

App en Kotlin y Jetpack Compose que consume la API Jolpica (compatible con Ergast):
https://api.jolpi.ca/ergast/f1/

Pantallas (barra inferior):
1. Calendario (centro y pantalla por defecto): carreras de la temporada, próxima carrera
   resaltada y sesiones del fin de semana al tocar la tarjeta.
2. Puntos: tabla de pilotos o constructores (se cambia con los chips), podio en oro, plata y bronce.
3. Pilotos: tarjetas con número, equipo, nacionalidad, nacimiento, puntos y enlace a Wikipedia.

El selector de temporada de la barra superior sirve para ver desde 1950 hasta la actual.

Tecnologías: Retrofit 2.9.0, Gson, Navigation Compose, Corrutinas, MVVM (data y presentation).
Requisitos: Android Studio Koala o superior, JDK 17, compileSdk y targetSdk 34.
