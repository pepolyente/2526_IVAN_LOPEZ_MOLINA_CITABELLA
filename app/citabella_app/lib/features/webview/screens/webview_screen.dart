// WebViewScreen — Pantalla principal de CitaBella.
//
// Es la única pantalla "activa" en tiempo de ejecución normal.
// Contiene:
//   • InAppWebView con la SPA Angular
//   • LoadingOverlay animado (fade out al cargar)
//   • OfflineScreen cuando no hay conexión
//   • PopScope para gestionar el botón Atrás de Android
//   • PullToRefresh opcional
//   • Configuración completa de InAppWebViewSettings
//
// Arquitectura:
//   WebViewScreen (widget) → consume WebViewStateController (ChangeNotifier)
//   WebViewStateController gestiona el estado y callbacks del WebView

import 'dart:developer' as dev;

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:provider/provider.dart';

import '../../../core/config/app_config.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/constants/app_strings.dart';
import '../../../core/services/connectivity_service.dart';
import '../../error/screens/offline_screen.dart';
import '../controllers/webview_state_controller.dart';
import '../widgets/loading_overlay.dart';

class WebViewScreen extends StatefulWidget {
  const WebViewScreen({super.key});

  @override
  State<WebViewScreen> createState() => _WebViewScreenState();
}

class _WebViewScreenState extends State<WebViewScreen> {
  late final WebViewStateController _controller;
  PullToRefreshController? _pullToRefreshController;

  @override
  void initState() {
    super.initState();

    // Instanciar el controller con el servicio de conectividad
    _controller = WebViewStateController(
      connectivityService: ConnectivityService(),
    );

    // Configurar PullToRefresh
    _pullToRefreshController = PullToRefreshController(
      settings: PullToRefreshSettings(enabled: true),
      onRefresh: () async {
        await _controller.webViewController?.reload();
        _pullToRefreshController?.endRefreshing();
      },
    );

    // Verificar conectividad e inicializar estado
    _controller.initialize();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  // ── Configuración del InAppWebView ────────────────────────────────────────
  InAppWebViewSettings _buildWebViewSettings() {
    return InAppWebViewSettings(
      // JavaScript — OBLIGATORIO para Angular
      javaScriptEnabled: AppConfig.webViewJavaScriptEnabled,

      // Storage — OBLIGATORIO para JWT en localStorage y sessionStorage
      domStorageEnabled: AppConfig.webViewDomStorageEnabled,
      databaseEnabled: AppConfig.webViewDatabaseEnabled,

      // Cache — mejora rendimiento en recargas
      cacheEnabled: AppConfig.webViewCacheEnabled,

      // HTTP mixto — NECESARIO para http:// en producción local
      // MIXED_CONTENT_ALWAYS_ALLOW permite cargar recursos HTTP desde HTTP
      mixedContentMode: MixedContentMode.MIXED_CONTENT_ALWAYS_ALLOW,

      // UX
      supportZoom: AppConfig.webViewSupportZoom,
      builtInZoomControls: false,
      displayZoomControls: false,

      // Scrollbars — el SPA Angular gestiona su propio scroll
      horizontalScrollBarEnabled: false,
      verticalScrollBarEnabled: false,

      // Evitar overscroll/bounce innecesario
      overScrollMode: OverScrollMode.NEVER,
      scrollsToTop: false,

      // Multimedia inline (por si Angular usa algún video/audio)
      allowsInlineMediaPlayback: true,
      mediaPlaybackRequiresUserGesture: false,

      // iOS — necesario para localStorage en WKWebView
      limitsNavigationsToAppBoundDomains: false,

      // Android — mejorar rendimiento de renderizado
      hardwareAcceleration: false,

      // Evitar que el WebView limpie caché al volver
      clearCache: false,

      // User agent (no modificar — Angular lo puede usar para detección)
      // userAgent: 'CitaBella/1.0 Flutter',

      // Debugging (desactivar en producción)
      isInspectable: AppConfig.debugLogs,
    );
  }

  // ── Build principal ───────────────────────────────────────────────────────
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<WebViewStateController>.value(
      value: _controller,
      child: Consumer<WebViewStateController>(
        builder: (context, controller, _) {
          return _buildByStatus(context, controller);
        },
      ),
    );
  }

  Widget _buildByStatus(BuildContext context, WebViewStateController controller) {
    switch (controller.status) {
      // Pantallas de error — sin WebView
      case WebViewStatus.offline:
      case WebViewStatus.serverError:
        return OfflineScreen(
          status: controller.status,
          onRetry: () => controller.retry(),
        );

      // WebView activo (loading o loaded) — siempre renderizar el WebView
      // para no perder el estado de Angular
      case WebViewStatus.initializing:
      case WebViewStatus.loading:
      case WebViewStatus.loaded:
        return _buildWebViewScaffold(context, controller);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Scaffold con WebView + Loading Overlay
  // ─────────────────────────────────────────────────────────────────────────
  Widget _buildWebViewScaffold(
    BuildContext context,
    WebViewStateController controller,
  ) {
    final brightness = Theme.of(context).brightness;
    final isDark = brightness == Brightness.dark;

    // Configurar la status bar para que el WebView la gestione
    SystemChrome.setSystemUIOverlayStyle(
      isDark
          ? SystemUiOverlayStyle.light.copyWith(
              statusBarColor: Colors.transparent,
            )
          : SystemUiOverlayStyle.dark.copyWith(
              statusBarColor: Colors.transparent,
            ),
    );

    return PopScope(
      // canPop: false → interceptamos el botón Atrás manualmente
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (didPop) return;
        await _handleBackButton(context, controller);
      },
      child: Scaffold(
        // Sin AppBar — la SPA Angular tiene su propio header
        backgroundColor: isDark
            ? AppColors.darkBackground
            : AppColors.lightBackground,
        body: Stack(
          children: [
            // ── WebView ──────────────────────────────────────────────────
            // Siempre presente en el árbol de widgets para no perder estado
            _buildWebView(controller),

            // ── Loading Overlay ──────────────────────────────────────────
            // Se muestra encima del WebView durante la carga
            // Fade-out automático cuando status == loaded
            LoadingOverlay(
              progress: controller.loadingProgress,
              isVisible: controller.status == WebViewStatus.initializing ||
                  controller.status == WebViewStatus.loading,
            ),
          ],
        ),
      ),
    );
  }

  // ─────────────────────────────────────────────────────────────────────────
  // InAppWebView principal
  // ─────────────────────────────────────────────────────────────────────────
  Widget _buildWebView(WebViewStateController controller) {
    return SafeArea(
      // bottom: false para que Angular pueda usar el espacio completo
      bottom: false,
      child: InAppWebView(
        initialUrlRequest: URLRequest(
          url: WebUri(AppConfig.baseUrl),
        ),
        initialSettings: _buildWebViewSettings(),
        pullToRefreshController: _pullToRefreshController,

        // ── Callbacks del ciclo de vida ──────────────────────────────────
        onWebViewCreated: (internalController) {
          controller.onWebViewCreated(internalController);
          _log('WebView creado, cargando: ${AppConfig.baseUrl}');
        },

        onLoadStart: (internalController, url) {
          _pullToRefreshController?.endRefreshing();
          controller.onLoadStart(internalController, url);
        },

        onProgressChanged: (internalController, progress) {
          controller.onProgressChanged(internalController, progress);
        },

        onLoadStop: (internalController, url) {
          _pullToRefreshController?.endRefreshing();
          controller.onLoadStop(internalController, url);
        },

        // ── Manejo de errores de red ──────────────────────────────────────
        onReceivedError: (internalController, request, error) {
          _pullToRefreshController?.endRefreshing();
          controller.onReceivedError(internalController, request, error);
        },

        onReceivedHttpError: (internalController, request, errorResponse) {
          controller.onReceivedHttpError(
              internalController, request, errorResponse);
        },

        // ── Control de navegación ─────────────────────────────────────────
        // Permite toda la navegación interna de Angular (rutas SPA)
        // y bloquea navegación externa (links a otras webs)
        shouldOverrideUrlLoading: (internalController, navigationAction) async {
          final url = navigationAction.request.url?.toString() ?? '';
          final baseUrl = AppConfig.baseUrl;

          // Permitir: misma URL base (Angular SPA)
          if (url.startsWith(baseUrl) || url == 'about:blank') {
            return NavigationActionPolicy.ALLOW;
          }

          // Bloquear y loggear navegación externa
          _log('⚠️  Navegación externa bloqueada: $url');
          return NavigationActionPolicy.CANCEL;
        },

        // ── Console logs de Angular (útil para debug) ─────────────────────
        onConsoleMessage: (internalController, consoleMessage) {
          if (AppConfig.debugLogs) {
            _log('[Angular Console] ${consoleMessage.messageLevel.toString()}: '
                '${consoleMessage.message}');
          }
        },

        // ── Permisos (no necesarios para CitaBella pero requeridos API) ──
        onPermissionRequest: (internalController, request) async {
          // CitaBella no requiere permisos especiales
          return PermissionResponse(
            resources: request.resources,
            action: PermissionResponseAction.DENY,
          );
        },
      ),
    );
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Gestión del botón Atrás de Android
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> _handleBackButton(
    BuildContext context,
    WebViewStateController controller,
  ) async {
    // Intentar navegar atrás en el historial del WebView (Angular router)
    final navigatedBack = await controller.navigateBack();
    if (navigatedBack) return; // ← consumido por el WebView

    // Si no hay historial, mostrar diálogo de confirmación para salir
    if (context.mounted) {
      await _showExitDialog(context);
    }
  }

  Future<void> _showExitDialog(BuildContext context) async {
    final brightness = Theme.of(context).brightness;
    final isDark = brightness == Brightness.dark;

    final shouldExit = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor:
            isDark ? AppColors.darkSurface : AppColors.lightSurface,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
          side: BorderSide(
            color: isDark ? AppColors.darkBorder : AppColors.lightBorder,
            width: 1,
          ),
        ),
        title: Text(
          AppStrings.exitTitle,
          style: TextStyle(
            fontSize: 17,
            fontWeight: FontWeight.w600,
            color:
                isDark ? const Color(0xFFF0EAD6) : AppColors.lightPrimary,
          ),
        ),
        content: Text(
          AppStrings.exitMessage,
          style: TextStyle(
            fontSize: 14,
            color: isDark ? AppColors.darkTextMuted : AppColors.lightTextMuted,
          ),
        ),
        actions: [
          // Cancelar — no salir
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            style: TextButton.styleFrom(
              foregroundColor:
                  isDark ? AppColors.darkTextMuted : AppColors.lightTextMuted,
            ),
            child: const Text(AppStrings.exitCancel),
          ),
          // Confirmar — salir de la app
          ElevatedButton(
            onPressed: () => Navigator.of(ctx).pop(true),
            style: ElevatedButton.styleFrom(
              backgroundColor:
                  isDark ? AppColors.darkPrimary : AppColors.lightPrimary,
              foregroundColor:
                  isDark ? AppColors.darkBackground : AppColors.white,
              elevation: 0,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
              ),
            ),
            child: const Text(AppStrings.exitConfirm),
          ),
        ],
      ),
    );

    if (shouldExit == true && context.mounted) {
      SystemNavigator.pop(); // Cierre limpio de la app
    }
  }

  void _log(String message) {
    if (AppConfig.debugLogs) {
      dev.log(message, name: 'WebViewScreen');
    }
  }
}
