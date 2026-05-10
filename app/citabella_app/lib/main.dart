import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import 'app.dart';

Future<void> main() async {
  // Garantizar que los bindings estén inicializados antes de cualquier
  // llamada a Platform Channels o plugins nativos
  WidgetsFlutterBinding.ensureInitialized();

  // ── Orientaciones soportadas ──────────────────────────────────────────────
  // Portrait UP/DOWN + Landscape LEFT/RIGHT
  await SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
    DeviceOrientation.landscapeLeft,
    DeviceOrientation.landscapeRight,
  ]);

  // ── Inicializar InAppWebView (Android requiere esto antes de runApp) ───────
  if (Platform.isAndroid) {
    await InAppWebViewController.setWebContentsDebuggingEnabled(
      // Solo habilitar debugging en modo debug
      // En producción esto debe ser false
      true, // Cambiar a false para release
    );
  }

  // ── Lanzar aplicación ─────────────────────────────────────────────────────
  runApp(const CitaBellaApp());
}
