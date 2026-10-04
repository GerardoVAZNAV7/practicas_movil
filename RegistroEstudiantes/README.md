# 📋 Sistema de Registro de Estudiantes

Aplicación móvil desarrollada en **Jetpack Compose** que integra los conceptos vistos en las Prácticas 1 a 5 de la materia de Desarrollo Móvil Nativo: controles interactivos de UI, navegación entre pantallas con paso de parámetros y persistencia local con `SharedPreferences`.

> Miniproyecto Integrador (Actividad 5.2) — Facultad de Ingeniería Mochis (FIM), Universidad Autónoma de Sinaloa (UAS).

## ✨ Características

- **Formulario de registro** con:
  - Matrícula y nombre completo (`OutlinedTextField`)
  - Carrera (menú desplegable / `ExposedDropdownMenu`)
  - Turno: Matutino / Vespertino (`RadioButton`)
  - Estatus: Activo / Inactivo (`Switch`)
- **Persistencia local**: cada estudiante registrado se guarda con `SharedPreferences` (lista serializada en JSON) y se recupera automáticamente al reabrir la app.
- **Navegación con paso de argumentos**: los datos del formulario viajan como parámetros de ruta hacia una pantalla de confirmación.
- **Pantalla de confirmación** que muestra de forma clara todos los datos recibidos.
- **Pantalla de registros guardados**: lista de tarjetas (`LazyColumn` + `Card`) con todos los estudiantes capturados, contador total y botón para volver al formulario.
- Interfaz simple con paleta de colores institucional y logo de la UAS.

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa |
| Material 3 | Componentes visuales |
| Navigation Compose | Navegación entre pantallas |
| SharedPreferences | Persistencia local |

## 📱 Requisitos

- Android Studio (Koala o superior recomendado)
- Android 14 (API 34) — `minSdk 26`
- Emulador o dispositivo físico

## 🚀 Cómo ejecutarlo

1. Clona este repositorio:
   ```bash
   git clone https://github.com/GerardoVAZNAV7/practicas_movil.git
   ```
2. Ábrelo con **Android Studio** → `Open` → selecciona la carpeta del proyecto.
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta ▶️ en un emulador o dispositivo con Android 14.

## 📂 Estructura del proyecto

```
app/src/main/java/com/example/registroestudiantes/
├── MainActivity.kt              # NavHost y configuración de rutas
├── data/
│   ├── Estudiante.kt            # Modelo de datos del estudiante
│   └── PreferencesManager.kt    # Persistencia con SharedPreferences
└── ui/
    ├── FormularioScreen.kt      # Pantalla 1: captura de datos
    ├── ConfirmacionScreen.kt    # Pantalla 2: muestra los datos recibidos
    ├── RegistrosScreen.kt       # Pantalla 3: tarjetas con los registros guardados
    └── theme/                   # Colores y tema Material3
```

## 🧭 Rutas de navegación

```
formulario ──Registrar──► confirmacion ──Ver registros──► registros
    ▲                                                │  │
    └──────────────── Volver al formulario ─────────┘  │
    └──────────────── Volver al formulario ◄───────────┘
```

## 🎥 Evidencia

Video/GIF de demostración: *(agrega aquí el enlace o inserta el GIF)*

## 📝 Notas

El logo (`uas_logo.png`) usado dentro de la app y como ícono del launcher puede sustituirse por el logo oficial de la institución si se requiere.

## 👤 Autor

Gerardo — Estudiante de Ingeniería en Software, FIM/UAS.
