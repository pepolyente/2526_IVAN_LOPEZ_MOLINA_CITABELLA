// AppConfig — Configuración centralizada de CitaBella Flutter Shell
//
// CÓMO CAMBIAR LA URL BASE:
//   1. Busca la constante [baseUrl] abajo
//   2. Cambia el valor según tu entorno
//   3. Guarda y ejecuta hot restart (no hot reload)
//
// ENTORNOS HABITUALES:
//   Android Emulator    → http://10.0.2.2
//   Dispositivo físico  → http://192.168.X.X  (IP de tu ordenador en la red local)
//   Producción          → https://tudominio.com
// ─────────────────────────────────────────────────────────────────────────────

class AppConfig {
  AppConfig._();

  // ── URL Principal ─────────────────────────────────────────────────────────
  //
  // MODIFICA ESTA LÍNEA CUANDO CAMBIES DE ENTORNO
  //
  // Android Emulator por defecto:
  static const String baseUrl = 'http://10.0.2.2:4200';
  //
  // Dispositivo físico en red local (ejemplo):
  // static const String baseUrl = 'http://192.168.1.100';
  //
  // Puerto Nginx (por defecto 80, no hace falta indicarlo):
  // static const String baseUrl = 'http://192.168.1.100:80';

  static const String appName = 'CitaBella';
  static const String appVersion = '1.0.0';

  // ── Tiempos de espera ─────────────────────────────────────────────────────

  /// Tiempo mínimo que se muestra el splash de Flutter (para evitar parpadeo)
  static const Duration splashMinDuration = Duration(milliseconds: 1800);

  /// Timeout para verificar si el servidor responde antes de cargar el WebView
  static const Duration serverCheckTimeout = Duration(seconds: 8);

  /// Tiempo de espera antes de reintentar conexión automáticamente
  static const Duration retryDelay = Duration(seconds: 2);

  // ── Configuración WebView ─────────────────────────────────────────────────

  /// Habilitar JavaScript (OBLIGATORIO para Angular)
  static const bool webViewJavaScriptEnabled = true;

  /// Habilitar DOM Storage (localStorage / sessionStorage — OBLIGATORIO para JWT)
  static const bool webViewDomStorageEnabled = true;

  /// Habilitar caché de recursos
  static const bool webViewCacheEnabled = true;

  /// Habilitar base de datos (IndexedDB)
  static const bool webViewDatabaseEnabled = true;

  /// Permitir zoom en el WebView (false = mejor UX móvil)
  static const bool webViewSupportZoom = false;

  // ── Debug ─────────────────────────────────────────────────────────────────

  /// Activar logs detallados en consola (desactivar en producción)
  static const bool debugLogs = true;
}
