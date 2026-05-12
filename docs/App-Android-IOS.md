# CitaBella — Aplicación Móvil Flutter

## 1. Arquitectura y Propósito

La aplicación móvil de **CitaBella** forma parte de la **Fase 1** y actúa como un **shell nativo Flutter** que contiene
la SPA Angular mediante un WebView avanzado.

La app no reimplementa la interfaz ni la lógica de negocio. En su lugar, carga directamente la versión web Angular del
sistema a través de **`flutter_inappwebview`**, mientras Flutter gestiona la capa nativa: splash screen, estados de
conectividad, tema visual y navegación con el botón Atrás de Android.

### Objetivos del enfoque

- Reutilización total del frontend Angular (interfaz, validaciones, JWT, API)
- Reducción del tiempo de desarrollo y mantenimiento unificado
- Consistencia visual entre plataformas (web, Android, iOS)
- Integración nativa progresiva sin reescribir el frontend

---

## 2. Stack Tecnológico Real

| Elemento      | Tecnología                           |
|---------------|--------------------------------------|
| Framework     | Flutter SDK `>=3.3.0 <4.0.0`         |
| WebView       | `flutter_inappwebview ^6.1.5`        |
| Estado        | `provider ^6.1.2` + `ChangeNotifier` |
| Conectividad  | `connectivity_plus ^6.1.1`           |
| Splash nativo | `flutter_native_splash ^2.4.3`       |
| Plataformas   | Android (iOS previsto)               |
| Distribución  | APK / Android App Bundle (AAB)       |

> **Nota importante:** el plugin utilizado es `flutter_inappwebview`, **no** `webview_flutter`. Ofrece control total
> sobre JavaScript, DOM Storage, IndexedDB, progreso de carga, pull-to-refresh y depuración remota.

---

## 3. Estructura del Proyecto

```
citabella_app/
├── lib/
│   ├── main.dart                          # Punto de entrada, inicialización
│   ├── app.dart                           # MaterialApp, temas, pantalla inicial
│   ├── core/
│   │   ├── config/
│   │   │   └── app_config.dart            # Configuración centralizada (URLs, timeouts, flags)
│   │   ├── constants/
│   │   │   ├── app_colors.dart            # Paleta light/dark basada en tokens Angular
│   │   │   └── app_strings.dart           # Textos de la UI nativa Flutter
│   │   ├── services/
│   │   │   └── connectivity_service.dart  # Verificación de red + servidor
│   │   └── theme/
│   │       └── app_theme.dart             # Material 3 light/dark theme
│   └── features/
│       ├── splash/
│       │   └── screens/splash_screen.dart # Splash animado (fade + scale)
│       ├── webview/
│       │   ├── controllers/
│       │   │   └── webview_state_controller.dart  # ChangeNotifier, estados del WebView
│       │   ├── screens/
│       │   │   └── webview_screen.dart    # Pantalla principal con InAppWebView
│       │   └── widgets/
│       │       └── loading_overlay.dart   # Overlay animado de carga con progress bar
│       └── error/
│           └── screens/offline_screen.dart  # Pantalla de error (sin red / servidor caído)
├── android/
│   └── app/src/main/AndroidManifest.xml   # Permisos, configuración nativa
├── flutter_native_splash.yaml             # Configuración splash nativo
└── pubspec.yaml                           # Dependencias
```

---

## 4. Configuración del Entorno (`app_config.dart`)

Toda la configuración se centraliza en `lib/core/config/app_config.dart`. Para cambiar de entorno basta con modificar
`baseUrl` y hacer **hot restart** (no hot reload).

```dart
class AppConfig {
  // ── Modifica esta línea según el entorno ──────────────────────────────
  static const String baseUrl = 'http://10.0.2.2:4200'; // Android Emulator (por defecto)
  // static const String baseUrl = 'http://192.168.1.100';  // Dispositivo físico en red local
  // static const String baseUrl = 'https://tudominio.com'; // Producción

  static const Duration splashMinDuration = Duration(milliseconds: 1800);
  static const Duration serverCheckTimeout = Duration(seconds: 8);
  static const Duration retryDelay = Duration(seconds: 2);

  static const bool webViewJavaScriptEnabled = true; // OBLIGATORIO para Angular
  static const bool webViewDomStorageEnabled = true; // OBLIGATORIO para JWT
  static const bool webViewCacheEnabled = true;
  static const bool webViewDatabaseEnabled = true; // IndexedDB
  static const bool debugLogs = true; // Cambiar a false en producción
}
```

### URLs por entorno

| Entorno            | URL                                                     |
|--------------------|---------------------------------------------------------|
| Android Emulator   | `http://10.0.2.2:4200`                                  |
| Dispositivo físico | `http://192.168.X.X` (IP del ordenador en la red local) |
| Nginx (producción) | `http://192.168.X.X:80` o `https://tudominio.com`       |

---

## 5. Flujo de Arranque

```
App lanzada
    │
    ▼
[Splash nativo]  ← flutter_native_splash (generado, no Flutter)
    │
    ▼
SplashScreen     ← animación fade + scale del logo (700 ms)
    │              espera splashMinDuration (1800 ms)
    ▼
WebViewScreen
    │
    ├─► ConnectivityService.checkConnectivity()
    │       ├── Sin red          → OfflineScreen (noNetwork)
    │       ├── Servidor caído   → OfflineScreen (serverError)
    │       └── OK               → InAppWebView carga Angular
    │
    ▼
LoadingOverlay (progreso real 0–100%)
    │
    ▼
Angular lista → fade out del overlay → app operativa
```

---

## 6. Estados del WebView (`WebViewStatus`)

El `WebViewStateController` (patrón `ChangeNotifier` + `Provider`) gestiona los siguientes estados:

| Estado         | Descripción                              | UI mostrada                                 |
|----------------|------------------------------------------|---------------------------------------------|
| `initializing` | Verificando conectividad antes de cargar | `LoadingOverlay`                            |
| `loading`      | InAppWebView cargando la SPA Angular     | `LoadingOverlay` con barra de progreso real |
| `loaded`       | Angular lista y completamente visible    | WebView (overlay hace fade out)             |
| `offline`      | Sin red disponible                       | `OfflineScreen` — icono wifi_off            |
| `serverError`  | Red OK pero servidor no responde         | `OfflineScreen` — icono dns_outlined        |

Desde cualquier pantalla de error, el botón **Reintentar** llama a `WebViewStateController.retry()`, que vuelve a
verificar la conectividad y recarga el WebView si procede.

---

## 7. Servicio de Conectividad (`ConnectivityService`)

El servicio distingue dos tipos de fallo:

1. **Sin red** (`noNetwork`): no hay WiFi, datos móviles ni Ethernet.
2. **Servidor inalcanzable** (`serverUnreachable`): hay red pero el socket TCP al host de `baseUrl` no responde en el
   timeout configurado (`serverCheckTimeout = 8s`).

```dart
enum ConnectivityStatus { online, noNetwork, serverUnreachable }
```

Esta distinción permite mostrar mensajes de error precisos al usuario ("Sin conexión a internet" vs. "Servidor en
mantenimiento").

---

## 8. Configuración del InAppWebView

El WebView se configura con los ajustes necesarios para que Angular funcione correctamente:

| Ajuste              | Valor          | Motivo                                   |
|---------------------|----------------|------------------------------------------|
| `javaScriptEnabled` | `true`         | Obligatorio para Angular                 |
| `domStorageEnabled` | `true`         | JWT en `localStorage` / `sessionStorage` |
| `databaseEnabled`   | `true`         | IndexedDB                                |
| `cacheEnabled`      | `true`         | Rendimiento en recargas                  |
| `mixedContentMode`  | `ALWAYS_ALLOW` | HTTP en desarrollo local                 |
| `supportZoom`       | `false`        | Mejor UX móvil                           |
| `isInspectable`     | `debugLogs`    | Depuración remota Chrome DevTools        |

El WebView siempre permanece en el árbol de widgets (incluso durante la carga) para no perder el estado de la SPA
Angular.

---

## 9. Flujo de Autenticación Móvil

El comportamiento es idéntico al navegador web. No requiere ninguna implementación adicional en Flutter:

1. El usuario abre la app → se muestra el splash.
2. El WebView carga la página pública de Angular.
3. El usuario pulsa **Acceder** → aparece el formulario de login.
4. Tras autenticación exitosa, el backend devuelve el JWT y Angular lo guarda en `localStorage` del propio WebView.
5. Las peticiones posteriores incluyen automáticamente `Authorization: Bearer <token>` gracias al `AuthInterceptor` de
   Angular.

---

## 10. Permisos Android (`AndroidManifest.xml`)

```xml

<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
```

Configuración adicional relevante en `<application>`:

```xml
android:usesCleartextTraffic="true"
        android:networkSecurityConfig="@xml/network_security_config"
        android:hardwareAccelerated="true"
```

`usesCleartextTraffic` es necesario para conectar a URLs HTTP en desarrollo. En producción con HTTPS puede desactivarse.

---

## 11. Tema Visual (Material 3)

La app usa **Material 3** y se adapta automáticamente al tema del sistema. La paleta está sincronizada con los design
tokens del frontend Angular (`src/styles/tokens.css`).

| Token      | Light            | Dark             |
|------------|------------------|------------------|
| Background | `#E4F0F6`        | `#0D0D0D`        |
| Surface    | `#FFFFFF`        | `#1A1A1A`        |
| Primary    | `#0A0F1E` (Navy) | `#C8A96E` (Gold) |
| Text muted | `#52697A`        | `#8A8076`        |
| Danger     | `#DC2626`        | `#F87171`        |
| Success    | `#16A34A`        | `#4ADE80`        |

El tema se aplica únicamente a las pantallas nativas Flutter (splash, overlay de carga, pantalla de error, diálogo de
salida). La interfaz principal vive en Angular.

---

## 12. Splash Screen Nativo

El splash nativo se genera con `flutter_native_splash` a partir de `flutter_native_splash.yaml`. Para regenerarlo:

```bash
dart run flutter_native_splash:create
```

Configuración:

| Ajuste         | Light                                   | Dark                                 |
|----------------|-----------------------------------------|--------------------------------------|
| Color de fondo | `#E4F0F6`                               | `#0D0D0D`                            |
| Imagen         | `assets/images/logo_splash.png`         | `assets/images/logo_splash_dark.png` |
| Android 12+    | Compatible con Splash Screen API nativa | ✓                                    |

---

## 13. Navegación con el Botón Atrás (Android)

El botón Atrás de Android se gestiona mediante `PopScope`:

1. Si el WebView tiene historial → navega atrás en el router de Angular.
2. Si no hay historial → muestra un diálogo de confirmación para salir de la app.
3. Al confirmar salida → `SystemNavigator.pop()` para un cierre limpio.

---

## 14. Pull-to-Refresh

Implementado con `PullToRefreshController` de `flutter_inappwebview`. Permite al usuario arrastrar hacia abajo para
recargar la SPA Angular. Se desactiva automáticamente al completar la carga.

---

## 15. Instalación y Ejecución

### Prerrequisitos

- Flutter SDK `>=3.3.0`
- Android Studio / VS Code con extensiones Flutter y Dart
- Emulador Android o dispositivo físico

### Pasos

```bash
# 1. Instalar dependencias
flutter pub get

# 2. Generar splash nativo (solo necesario la primera vez o al cambiar assets)
dart run flutter_native_splash:create

# 3. Configurar la URL base en lib/core/config/app_config.dart
#    (ver sección 4 para los valores por entorno)

# 4. Ejecutar la app
flutter run

# 5. Compilar APK de release
flutter build apk --release

# 6. Compilar Android App Bundle (recomendado para Play Store)
flutter build appbundle --release
```

> Antes de compilar en modo release, cambiar `AppConfig.debugLogs = false` y `setWebContentsDebuggingEnabled(false)` en
`main.dart`.

---

## 16. Limitaciones Actuales

**Dependencia del frontend web.** Cualquier cambio en Angular se refleja automáticamente en la app móvil (ventaja), pero
también comparte posibles problemas de rendimiento móvil de la SPA.

**Funcionalidades nativas no implementadas.** Las siguientes características están previstas para fases futuras y
actualmente no están disponibles:

- Notificaciones push (FCM)
- Acceso a cámara u otros recursos nativos
- Soporte offline / caché local de la SPA

**Requiere conexión.** Toda la lógica reside en el servidor. No hay confirmación de soporte offline más allá de la
pantalla de error con botón de reintento.

---

## 17. Mejoras Futuras Sugeridas

**Notificaciones Push.** Integrar Firebase Cloud Messaging (FCM) conectado al módulo de notificaciones de la Fase 3 para
recordatorios y avisos automáticos.

**Componentes Flutter nativos.** Migrar progresivamente pantallas críticas (agenda diaria, creación rápida de citas,
panel resumen) hacia Flutter nativo, manteniendo el resto en WebView.

**Optimización del arranque.** El WebView realiza una conexión TCP al servidor antes de cargar. En redes lentas esto
puede añadir latencia perceptible. Una caché del shell HTML de Angular reduciría el tiempo de primera carga.

**Producción con HTTPS.** Desactivar `usesCleartextTraffic` y usar una URL HTTPS en `AppConfig.baseUrl` para
distribución en Google Play.