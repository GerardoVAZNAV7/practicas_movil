# Práctica 13 – Sensores (Acelerómetro y detección de sacudida)

Aplicación nativa Android (Kotlin + Jetpack Compose, **Android 14 / API 34**) que:

- Lee el acelerómetro (`Sensor.TYPE_ACCELEROMETER`) con `SensorManager` y `SensorEventListener`.
- Muestra en tiempo real los ejes **X, Y y Z** (m/s²).
- Calcula la aceleración vectorial `sqrt(x² + y² + z²) / 9.81` (en G) y la compara contra un umbral (2.7 G).
- Al detectar una sacudida, el fondo cambia a rojo, aparece el mensaje "¡SACUDIDA DETECTADA!" y se incrementa un contador.
- Usa `DisposableEffect` para registrar el sensor al abrir la pantalla y liberarlo al cerrarla.

## Estructura

```
practica13/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/practica13/
        │   ├── MainActivity.kt
        │   └── SensorScreen.kt
        └── res/values/{strings,themes}.xml
```

## Cómo ejecutarlo

1. Abrir la carpeta en Android Studio (Koala o superior) y esperar el *Gradle Sync*.
2. Ejecutar en un dispositivo físico o emulador con API 34.
3. En el emulador: `⋮ > Virtual sensors > Device Pose` y mover los sliders, o usar *Move* para simular la sacudida.

## Ajustes

En `SensorScreen.kt`: `UMBRAL_SHAKE_G` (sensibilidad) y `TIEMPO_ENTRE_SHAKES_MS` (anti-rebote).
