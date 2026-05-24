// Textos de la interfaz nativa Flutter (splash, errores, diálogos).
// Los textos de la SPA Angular se gestionan completamente en Angular.

class AppStrings {
  AppStrings._();

  // ── Nombre de la app ──────────────────────────────────────────────────────
  static const String appName        = 'CitaBella';
  static const String appTagline     = 'Tu centro de belleza';

  // ── Pantalla Offline / Error ──────────────────────────────────────────────
  static const String offlineTitle       = 'Servidor en mantenimiento';
  static const String offlineDescription = 'No se pudo conectar con el servidor.\n'
      'Comprueba que el servidor esté activo\n'
      'y que estés conectado a la red.';
  static const String retryButton        = 'Reintentar';

  static const String noInternetTitle       = 'Sin conexión a internet';
  static const String noInternetDescription = 'Parece que no tienes conexión a internet.\n'
      'Comprueba tu red Wi-Fi o datos móviles\n'
      'e inténtalo de nuevo.';

  // ── Loading ───────────────────────────────────────────────────────────────
  static const String loadingText    = 'Cargando...';
  static const String connectingText = 'Conectando con el servidor...';

  // ── Diálogo salir ─────────────────────────────────────────────────────────
  static const String exitTitle       = 'Salir de CitaBella';
  static const String exitMessage     = '¿Deseas cerrar la aplicación?';
  static const String exitConfirm     = 'Salir';
  static const String exitCancel      = 'Cancelar';
}
