// WebViewStateController — Gestiona el estado completo del WebView de CitaBella.
//
// Estados posibles (WebViewStatus):
//   • initializing    → verificando conectividad antes de cargar
//   • loading         → WebView cargando la SPA Angular (muestra progress bar)
//   • loaded          → Angular lista, WebView completamente visible
//   • offline         → sin red disponible
//   • serverError     → red OK pero servidor no responde
//
// Patrón: ChangeNotifier + Provider
// El WebViewScreen escucha cambios y renderiza la UI correspondiente.

import 'dart:developer' as dev;

import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import '../../../core/config/app_config.dart';
import '../../../core/services/connectivity_service.dart';

// ─────────────────────────────────────────────────────────────────────────────
// Enum de estados
// ─────────────────────────────────────────────────────────────────────────────
enum WebViewStatus {
  initializing,
  loading,
  loaded,
  offline,
  serverError,
}

// ─────────────────────────────────────────────────────────────────────────────
// WebViewStateController
// ─────────────────────────────────────────────────────────────────────────────
class WebViewStateController extends ChangeNotifier {
  WebViewStateController({
    required ConnectivityService connectivityService,
  }) : _connectivityService = connectivityService;

  final ConnectivityService _connectivityService;

  // ── Estado público ────────────────────────────────────────────────────────
  WebViewStatus _status = WebViewStatus.initializing;
  WebViewStatus get status => _status;

  /// Progreso de carga del WebView [0–100]
  int _loadingProgress = 0;
  int get loadingProgress => _loadingProgress;

  /// URL actual cargada en el WebView
  String? _currentUrl;
  String? get currentUrl => _currentUrl;

  /// Referencia al InAppWebViewController (asignada desde el widget)
  InAppWebViewController? _webViewController;
  InAppWebViewController? get webViewController => _webViewController;

  /// Indica si hay una carga en curso
  bool get isLoading =>
      _status == WebViewStatus.loading || _status == WebViewStatus.initializing;

  // ── Inicialización ────────────────────────────────────────────────────────
  /// Verifica conectividad antes de mostrar el WebView.
  /// Llamar desde initState del WebViewScreen.
  Future<void> initialize() async {
    _setStatus(WebViewStatus.initializing);

    await Future.delayed(const Duration(milliseconds: 300)); // micro-delay UX

    final connectivityStatus =
        await _connectivityService.checkConnectivity();

    switch (connectivityStatus) {
      case ConnectivityStatus.online:
        _setStatus(WebViewStatus.loading);
        break;
      case ConnectivityStatus.noNetwork:
        _setStatus(WebViewStatus.offline);
        break;
      case ConnectivityStatus.serverUnreachable:
        _setStatus(WebViewStatus.serverError);
        break;
    }
  }

  // ── Callbacks del WebView ─────────────────────────────────────────────────

  void onWebViewCreated(InAppWebViewController controller) {
    _webViewController = controller;
    _log('WebView creado');
  }

  void onLoadStart(InAppWebViewController controller, WebUri? url) {
    _currentUrl = url?.toString();
    _loadingProgress = 0;
    if (_status != WebViewStatus.loading) {
      _setStatus(WebViewStatus.loading);
    }
    _log('Cargando: $url');
  }

  void onProgressChanged(InAppWebViewController controller, int progress) {
    _loadingProgress = progress;
    notifyListeners();
    _log('Progreso: $progress%');
  }

  void onLoadStop(InAppWebViewController controller, WebUri? url) {
    _currentUrl = url?.toString();
    _loadingProgress = 100;
    _setStatus(WebViewStatus.loaded);
    _log('Carga completada: $url');
  }

  void onReceivedError(
    InAppWebViewController controller,
    WebResourceRequest request,
    WebResourceError error,
  ) {
    // Solo reaccionar a errores del frame principal (no de sub-recursos)
    final isMainFrame = request.isForMainFrame ?? false;
    if (!isMainFrame) return;

    _log('Error en frame principal: ${error.description} (código: ${error.type})');

    // Verificar qué tipo de error es para mostrar la pantalla correcta
    _handleMainFrameError(error);
  }

  void onReceivedHttpError(
    InAppWebViewController controller,
    WebResourceRequest request,
    WebResourceResponse errorResponse,
  ) {
    final isMainFrame = request.isForMainFrame ?? false;
    if (!isMainFrame) return;

    final statusCode = errorResponse.statusCode ?? 0;
    _log('HTTP error $statusCode en frame principal');

    // 502/503/504 → servidor no disponible
    if (statusCode >= 500) {
      _setStatus(WebViewStatus.serverError);
    }
  }

  // ── Retry ─────────────────────────────────────────────────────────────────
  /// Reintentar conexión — verifica de nuevo y recarga si hay conexión.
  Future<void> retry() async {
    _log('Reintentando conexión...');
    _setStatus(WebViewStatus.initializing);

    await Future.delayed(AppConfig.retryDelay);

    final connectivityStatus =
        await _connectivityService.checkConnectivity();

    switch (connectivityStatus) {
      case ConnectivityStatus.online:
        _setStatus(WebViewStatus.loading);
        // Recargar el WebView
        await _webViewController?.loadUrl(
          urlRequest: URLRequest(url: WebUri(AppConfig.baseUrl)),
        );
        break;
      case ConnectivityStatus.noNetwork:
        _setStatus(WebViewStatus.offline);
        break;
      case ConnectivityStatus.serverUnreachable:
        _setStatus(WebViewStatus.serverError);
        break;
    }
  }

  // ── Navegación hacia atrás ────────────────────────────────────────────────
  /// Retrocede en el historial del WebView si es posible.
  /// Devuelve true si se pudo retroceder (el botón atrás se consumió).
  /// Devuelve false si no hay historial (se debe mostrar diálogo de salida).
  Future<bool> navigateBack() async {
    if (_webViewController == null) return false;

    final canGoBack = await _webViewController!.canGoBack();
    if (canGoBack) {
      await _webViewController!.goBack();
      _log('Navegado hacia atrás en WebView');
      return true;
    }

    _log('No hay historial — ofrecer salir de la app');
    return false;
  }

  // ── Helpers privados ──────────────────────────────────────────────────────

  void _setStatus(WebViewStatus newStatus) {
    if (_status == newStatus) return;
    _status = newStatus;
    _log('Estado → $newStatus');
    notifyListeners();
  }

  /// Clasifica errores de red para mostrar la pantalla correcta
  void _handleMainFrameError(WebResourceError error) {
    final description = error.description.toLowerCase();

    final isOffline =
        description.contains('internet') ||
            description.contains('network') ||
            description.contains('host') ||
            description.contains('dns') ||
            description.contains('timeout');

    if (isOffline) {
      _setStatus(WebViewStatus.offline);
    } else {
      _setStatus(WebViewStatus.serverError);
    }
  }

  void _log(String message) {
    if (AppConfig.debugLogs) {
      dev.log(message, name: 'WebViewStateController');
    }
  }
}
