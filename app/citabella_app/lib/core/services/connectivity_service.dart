// ConnectivityService — Detecta disponibilidad de red Y del servidor CitaBella.
//
// Responsabilidades:
//   1. Verificar si hay red disponible (wifi / datos móviles)
//   2. Verificar si el servidor Nginx/Spring Boot responde en la URL base
//   3. Distinguir entre "sin internet" y "servidor caído"
//
// Uso:
//   final service = ConnectivityService();
//   final result = await service.checkConnectivity();

import 'dart:async';
import 'dart:developer' as dev;
import 'dart:io';

import 'package:connectivity_plus/connectivity_plus.dart';
import '../config/app_config.dart';

// ─────────────────────────────────────────────────────────────────────────────
// Resultado de la comprobación de conectividad
// ─────────────────────────────────────────────────────────────────────────────
enum ConnectivityStatus {
  /// Red disponible Y servidor responde → OK
  online,

  /// No hay red (wifi ni datos) → mostrar "sin internet"
  noNetwork,

  /// Hay red pero el servidor no responde → mostrar "servidor en mantenimiento"
  serverUnreachable,
}

// ─────────────────────────────────────────────────────────────────────────────
// ConnectivityService
// ─────────────────────────────────────────────────────────────────────────────
class ConnectivityService {
  final Connectivity _connectivity = Connectivity();

  // ── Comprobación completa (red + servidor) ────────────────────────────────
  /// Devuelve el estado real de la conectividad.
  /// Primero verifica la red, luego intenta alcanzar el servidor.
  Future<ConnectivityStatus> checkConnectivity() async {
    // 1. Verificar disponibilidad de red
    final hasNetwork = await _hasNetworkConnection();

    if (!hasNetwork) {
      _log('❌ Sin red disponible');
      return ConnectivityStatus.noNetwork;
    }

    // 2. Verificar si el servidor responde
    final serverReachable = await _isServerReachable();

    if (!serverReachable) {
      _log('⚠️ Red disponible pero servidor no responde: ${AppConfig.baseUrl}');
      return ConnectivityStatus.serverUnreachable;
    }

    _log('✅ Conectividad OK → ${AppConfig.baseUrl}');
    return ConnectivityStatus.online;
  }

  // ── Solo verificar red (sin TCP al servidor) ──────────────────────────────
  Future<bool> _hasNetworkConnection() async {
    try {
      final results = await _connectivity.checkConnectivity();
      final hasConnection = results.any((r) =>
          r == ConnectivityResult.wifi ||
          r == ConnectivityResult.mobile ||
          r == ConnectivityResult.ethernet);
      _log('Red detectada: $results → hasConnection=$hasConnection');
      return hasConnection;
    } catch (e) {
      _log('Error comprobando red: $e');
      return false;
    }
  }

  // ── Verificar si el servidor Nginx responde ───────────────────────────────
  /// Hace una conexión TCP/HTTP ligera al host del servidor.
  /// No descarga el HTML completo — solo verifica que el socket responde.
  Future<bool> _isServerReachable() async {
    try {
      final uri = Uri.parse(AppConfig.baseUrl);
      final host = uri.host;
      final port = uri.port > 0 ? uri.port : (uri.scheme == 'https' ? 443 : 80);

      _log('Verificando servidor → $host:$port');

      final socket = await Socket.connect(
        host,
        port,
        timeout: AppConfig.serverCheckTimeout,
      );
      socket.destroy();

      _log('Servidor responde en $host:$port');
      return true;
    } on SocketException catch (e) {
      _log('Servidor no alcanzable: ${e.message}');
      return false;
    } on TimeoutException {
      _log('Timeout alcanzando servidor tras ${AppConfig.serverCheckTimeout.inSeconds}s');
      return false;
    } catch (e) {
      _log('Error inesperado verificando servidor: $e');
      return false;
    }
  }

  // ── Stream de cambios de red ──────────────────────────────────────────────
  /// Stream que emite cuando cambia el tipo de conexión.
  /// Útil para reaccionar automáticamente a cambios de red.
  Stream<List<ConnectivityResult>> get onConnectivityChanged =>
      _connectivity.onConnectivityChanged;

  // ── Logger interno ────────────────────────────────────────────────────────
  void _log(String message) {
    if (AppConfig.debugLogs) {
      dev.log(message, name: 'ConnectivityService');
    }
  }
}
