import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'core/config/app_config.dart';
import 'core/theme/app_theme.dart';
import 'features/splash/screens/splash_screen.dart';

class CitaBellaApp extends StatelessWidget {
  const CitaBellaApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // ── Metadata ───────────────────────────────────────────────────────
      title: AppConfig.appName,
      debugShowCheckedModeBanner: false,

      // ── Temas ──────────────────────────────────────────────────────────
      // Se adapta automáticamente al tema del sistema
      theme: AppTheme.light,
      darkTheme: AppTheme.dark,
      themeMode: ThemeMode.system,

      // ── Pantalla inicial ───────────────────────────────────────────────
      home: const SplashScreen(),

      // ── Builder global ────────────────────────────────────────────────
      // Configura status bar transparente en toda la app
      builder: (context, child) {
        // Forzar StatusBar transparente para que el WebView pueda extenderse bajo ella limpiamente
        SystemChrome.setSystemUIOverlayStyle(
          const SystemUiOverlayStyle(
            statusBarColor: Colors.transparent,
          ),
        );
        return child!;
      },
    );
  }
}
