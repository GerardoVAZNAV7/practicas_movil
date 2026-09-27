# Práctica 5 — SharedPreferences (Android 14 / Jetpack Compose)

Aplicación de ejemplo hecha con **Kotlin + Jetpack Compose** que muestra cómo
guardar, recuperar y borrar datos simples de forma persistente en Android
usando `SharedPreferences`, sin usar layouts XML.

Repositorio general de prácticas: https://github.com/GerardoVAZNAV7/practicas_movil.git

## ¿Qué hace la app?

Una pantalla de configuración con:
- Un campo de texto para el **nombre de usuario**.
- Un switch para **activar/desactivar notificaciones**.
- Un switch para **activar el tema oscuro** (cambia el color de la app al instante).
- Botones para **Guardar**, **Recargar** y **Restablecer** esos datos.

Lo importante: si cierras la app por completo y la vuelves a abrir, tus datos
siguen ahí. Eso es justamente lo que hace `SharedPreferences` — persistir
información en un archivo interno del dispositivo, en pares clave-valor.

## Requisitos

- [Android Studio](https://developer.android.com/studio) (versión reciente, Koala o más nueva)
- JDK 17 (Android Studio ya lo trae integrado)
- Un emulador o un dispositivo físico con **Android 8.0 (API 26)** o superior
  — probado sobre **Android 14 (API 34)**

## Cómo clonar y ejecutar

```bash
git clone https://github.com/GerardoVAZNAV7/practicas_movil.git
cd practicas_movil/practica05
```

1. Abre la carpeta `practica05` con Android Studio (**File → Open**).
2. Espera a que Gradle sincronice las dependencias (necesita internet la
   primera vez).
3. Selecciona un emulador o conecta tu teléfono con la depuración USB activada.
4. Presiona **Run ▶** o `Shift + F10`.

Si Android Studio pide regenerar el *Gradle Wrapper*, dale "Yes" — es normal
y no afecta el proyecto.

## Estructura del proyecto

```
app/src/main/java/com/example/practica05/
├── MainActivity.kt           # Punto de entrada, solo llama a FormScreen()
├── FormScreen.kt             # Toda la interfaz y la lógica de guardar/cargar/borrar
└── data/
    └── PreferencesManager.kt # Clase que envuelve SharedPreferences
```

## Cómo probar que la persistencia funciona

1. Escribe un nombre, activa los switches y presiona **Guardar Preferencias**.
2. Cierra la app por completo (deslízala fuera de la vista de recientes, o
   fuérzala a detenerse desde Ajustes → Apps).
3. Vuelve a abrirla: el nombre y los switches deben aparecer tal como los
   dejaste.
4. Presiona **Restablecer Configuración** para borrar todo y volver a los
   valores por defecto.

## Licencia

Proyecto con fines académicos y de aprendizaje. Puedes usarlo, modificarlo y
replicarlo libremente como base para tus propias prácticas.
