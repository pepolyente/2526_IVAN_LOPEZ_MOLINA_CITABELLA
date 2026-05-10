// SplashScreen — Pantalla de arranque de CitaBella.
//
// Flujo:
//   1. Se muestra inmediatamente tras el splash nativo (flutter_native_splash)
//   2. Espera [AppConfig.splashMinDuration] para que la transición sea suave
//   3. Navega automáticamente a WebViewScreen
//
// NOTA: La verificación de conectividad NO se hace aquí.
//       Se hace dentro de WebViewStateController.initialize()
//       para que el WebView ya esté en el árbol cuando se decide
//       qué pantalla mostrar.
//
// Animaciones:
//   • Logo: fadeIn + scale desde 0.8 → 1.0
//   • Tagline: fadeIn con delay

import 'package:flutter/material.dart';
import '../../../core/config/app_config.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/constants/app_strings.dart';
import '../../webview/screens/webview_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen>
    with TickerProviderStateMixin {
  // Animación de entrada del logo
  late final AnimationController _logoController;
  late final Animation<double> _logoOpacity;
  late final Animation<double> _logoScale;

  // Animación de entrada del tagline
  late final AnimationController _taglineController;
  late final Animation<double> _taglineOpacity;

  @override
  void initState() {
    super.initState();

    // ── Animación del logo ─────────────────────────────────────────────────
    _logoController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 700),
    );

    _logoOpacity = Tween<double>(begin: 0, end: 1).animate(
      CurvedAnimation(parent: _logoController, curve: Curves.easeOut),
    );

    _logoScale = Tween<double>(begin: 0.82, end: 1.0).animate(
      CurvedAnimation(parent: _logoController, curve: Curves.easeOutBack),
    );

    // ── Animación del tagline ──────────────────────────────────────────────
    _taglineController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 500),
    );

    _taglineOpacity = Tween<double>(begin: 0, end: 1).animate(
      CurvedAnimation(parent: _taglineController, curve: Curves.easeOut),
    );

    // ── Secuencia de arranque ──────────────────────────────────────────────
    _startSplashSequence();
  }

  Future<void> _startSplashSequence() async {
    // Pequeño delay inicial para que el frame se pinte
    await Future.delayed(const Duration(milliseconds: 100));
    if (!mounted) return;

    // Animar logo
    _logoController.forward();

    // Animar tagline con delay
    await Future.delayed(const Duration(milliseconds: 350));
    if (!mounted) return;
    _taglineController.forward();

    // Esperar duración mínima del splash
    await Future.delayed(AppConfig.splashMinDuration);
    if (!mounted) return;

    // Navegar a WebViewScreen (reemplaza el splash — no se puede volver)
    Navigator.of(context).pushReplacement(
      PageRouteBuilder(
        pageBuilder: (_, __, ___) => const WebViewScreen(),
        transitionDuration: const Duration(milliseconds: 400),
        transitionsBuilder: (_, animation, __, child) {
          return FadeTransition(
            opacity: CurvedAnimation(
              parent: animation,
              curve: Curves.easeIn,
            ),
            child: child,
          );
        },
      ),
    );
  }

  @override
  void dispose() {
    _logoController.dispose();
    _taglineController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final brightness = Theme.of(context).brightness;
    final isDark = brightness == Brightness.dark;

    final bgColor      = isDark ? AppColors.darkBackground   : AppColors.lightBackground;
    final primaryColor = isDark ? AppColors.darkPrimary      : AppColors.lightPrimary;
    final mutedColor   = isDark ? AppColors.darkTextMuted    : AppColors.lightTextMuted;

    return Scaffold(
      backgroundColor: bgColor,
      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Spacer(flex: 3),

              // ── Logo animado ─────────────────────────────────────────────
              FadeTransition(
                opacity: _logoOpacity,
                child: ScaleTransition(
                  scale: _logoScale,
                  child: Column(
                    children: [
                      // Logo de imagen o fallback tipográfico
                      _buildLogo(isDark, primaryColor),

                      const SizedBox(height: 22),

                      // Nombre de la app
                      Text(
                        AppStrings.appName,
                        style: TextStyle(
                          fontSize: 30,
                          fontWeight: FontWeight.w700,
                          color: primaryColor,
                          letterSpacing: 1.2,
                        ),
                      ),
                    ],
                  ),
                ),
              ),

              const SizedBox(height: 10),

              // ── Tagline animado ──────────────────────────────────────────
              FadeTransition(
                opacity: _taglineOpacity,
                child: Text(
                  AppStrings.appTagline,
                  style: TextStyle(
                    fontSize: 15,
                    color: mutedColor,
                    fontWeight: FontWeight.w400,
                    letterSpacing: 0.4,
                  ),
                ),
              ),

              const Spacer(flex: 4),

              // ── Footer con versión ───────────────────────────────────────
              FadeTransition(
                opacity: _taglineOpacity,
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 28),
                  child: Text(
                    'v${AppConfig.appVersion}',
                    style: TextStyle(
                      fontSize: 11,
                      color: mutedColor.withValues(alpha: 0.5),
                      letterSpacing: 0.8,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildLogo(bool isDark, Color primaryColor) {
    return Image.asset(
      isDark
          ? 'assets/images/logo_splash_dark.png'
          : 'assets/images/logo_splash.png',
      width: 110,
      height: 110,
      errorBuilder: (_, __, ___) {
        // Fallback si no existe el asset
        return Container(
          width: 100,
          height: 100,
          decoration: BoxDecoration(
            color: isDark ? AppColors.darkSurfaceAlt : AppColors.lightSurfaceAlt,
            shape: BoxShape.circle,
            border: Border.all(
              color: isDark ? AppColors.darkBorder : AppColors.lightBorder,
              width: 1.5,
            ),
          ),
          child: Center(
            child: Text(
              'CB',
              style: TextStyle(
                fontSize: 34,
                fontWeight: FontWeight.w700,
                color: primaryColor,
                letterSpacing: 2,
              ),
            ),
          ),
        );
      },
    );
  }
}
