# practica10 – Uso de Cámara (Jetpack Compose)

Desarrollo Móvil Nativo · Android 14 (API 34)

- Captura de fotos con `ActivityResultContracts.TakePicturePreview()`
- Permiso `CAMERA` pedido en tiempo de ejecución con `ActivityResultContracts.RequestPermission()`
- Las fotos se muestran en un `LazyVerticalGrid`

## Cómo abrirlo
1. Android Studio (Koala o más reciente) → File > Open → carpeta `practica10`
2. Esperar el Gradle Sync (necesita internet)
3. Ejecutar en emulador (con cámara activa) o celular con Android 14

## Flujo para las capturas del PDF
1. Pantalla inicial con el aviso de permiso
2. Diálogo del sistema pidiendo el permiso
3. Cámara abierta tomando la foto
4. Galería con la foto en la cuadrícula
