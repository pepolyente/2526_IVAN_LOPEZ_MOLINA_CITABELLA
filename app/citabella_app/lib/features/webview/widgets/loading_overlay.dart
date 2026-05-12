// LoadingOverlay — Pantalla de carga que se muestra mientras Angular arranca.
//
// Muestra:
//   • Fondo con el color de la marca CitaBella
//   • Logo de CitaBella centrado
//   • LinearProgressIndicator con el progreso real del WebView
//   • Texto de estado ("Cargando...", "Conectando...")
//
// Se anima con FadeTransition al desaparecer cuando Angular está lista.

import 'package:flutter/material.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/constants/app_strings.dart';

class LoadingOverlay extends StatefulWidget {
  const LoadingOverlay({
    super.key,
    required this.progress,
    required this.isVisible,
  });

  /// Progreso de carga [0–100]
  final int progress;

  /// Controla la visibilidad con animación de fade
  final bool isVisible;

  @override
  State<LoadingOverlay> createState() => _LoadingOverlayState();
}

class _LoadingOverlayState extends State<LoadingOverlay>
    with SingleTickerProviderStateMixin {
  late final AnimationController _fadeController;
  late final Animation<double> _fadeAnimation;

  @override
  void initState() {
    super.initState();
    _fadeController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 500),
      value: 1.0, // empieza visible
    );
    _fadeAnimation = CurvedAnimation(
      parent: _fadeController,
      curve: Curves.easeOut,
    );
  }

  @override
  void didUpdateWidget(LoadingOverlay oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (!widget.isVisible && oldWidget.isVisible) {
      _fadeController.reverse(); // fade out al terminar de cargar
    }
    if (widget.isVisible && !oldWidget.isVisible) {
      _fadeController.forward(); // fade in si vuelve a cargar
    }
  }

  @override
  void dispose() {
    _fadeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FadeTransition(
      opacity: _fadeAnimation,
      child: IgnorePointer(
        // Cuando es invisible, no intercepta eventos táctiles
        ignoring: !widget.isVisible,
        child: _buildContent(context),
      ),
    );
  }

  Widget _buildContent(BuildContext context) {
    final brightness = Theme.of(context).brightness;
    final bgColor    = AppColors.loadingBackground(brightness);
    final fgColor    = AppColors.progressBarColor(brightness);
    final mutedColor = brightness == Brightness.dark
        ? AppColors.darkTextMuted
        : AppColors.lightTextMuted;

    // Progreso normalizado [0.0 – 1.0]
    final normalizedProgress = widget.progress / 100.0;

    return Container(
      width: double.infinity,
      height: double.infinity,
      color: bgColor,
      child: SafeArea(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Spacer(flex: 3),

            // ── Logo ────────────────────────────────────────────────────
            _CitabellaLogo(brightness: brightness),

            const SizedBox(height: 48),

            // ── Barra de progreso ────────────────────────────────────────
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 64),
              child: Column(
                children: [
                  ClipRRect(
                    borderRadius: BorderRadius.circular(4),
                    child: LinearProgressIndicator(
                      value: normalizedProgress > 0 ? normalizedProgress : null,
                      backgroundColor: brightness == Brightness.dark
                          ? AppColors.darkBorder
                          : AppColors.lightBorder,
                      valueColor: AlwaysStoppedAnimation<Color>(fgColor),
                      minHeight: 3,
                    ),
                  ),
                  const SizedBox(height: 16),
                  // Texto de estado
                  AnimatedSwitcher(
                    duration: const Duration(milliseconds: 300),
                    child: Text(
                      widget.progress > 10
                          ? AppStrings.loadingText
                          : AppStrings.connectingText,
                      key: ValueKey(widget.progress > 10),
                      style: TextStyle(
                        fontSize: 13,
                        color: mutedColor,
                        fontWeight: FontWeight.w500,
                        letterSpacing: 0.3,
                      ),
                    ),
                  ),
                ],
              ),
            ),

            const Spacer(flex: 4),

            // ── Versión en footer ────────────────────────────────────────
            Padding(
              padding: const EdgeInsets.only(bottom: 24),
              child: Text(
                AppStrings.appName,
                style: TextStyle(
                  fontSize: 12,
                  color: mutedColor.withValues(alpha: 0.5),
                  fontWeight: FontWeight.w500,
                  letterSpacing: 1.5,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────────────────────────
// Logo de CitaBella — usa asset si existe, fallback a logotipo tipográfico
// ─────────────────────────────────────────────────────────────────────────────
class _CitabellaLogo extends StatelessWidget {
  const _CitabellaLogo({required this.brightness});
  final Brightness brightness;

  @override
  Widget build(BuildContext context) {
    final isDark = brightness == Brightness.dark;
    final primaryColor =
        isDark ? AppColors.darkPrimary : AppColors.lightPrimary;

    return Column(
      children: [
        // Intentar cargar el logo desde assets
        // Si no existe, muestra el logotipo tipográfico
        Image.asset(
          isDark
              ? 'assets/images/logo_splash_dark.png'
              : 'assets/images/logo_splash.png',
          width: 120,
          height: 120,
          errorBuilder: (_, __, ___) =>
              _FallbackLogo(color: primaryColor, isDark: isDark),
        ),

        const SizedBox(height: 20),

        // Nombre de la app
        Text(
          AppStrings.appName,
          style: TextStyle(
            fontSize: 28,
            fontWeight: FontWeight.w700,
            color: primaryColor,
            letterSpacing: 1.0,
          ),
        ),
        const SizedBox(height: 6),
        Text(
          AppStrings.appTagline,
          style: TextStyle(
            fontSize: 14,
            color: isDark
                ? AppColors.darkTextMuted
                : AppColors.lightTextMuted,
            fontWeight: FontWeight.w400,
            letterSpacing: 0.5,
          ),
        ),
      ],
    );
  }
}

/// Logo fallback tipográfico — se muestra si no hay imagen de asset
class _FallbackLogo extends StatelessWidget {
  const _FallbackLogo({required this.color, required this.isDark});
  final Color color;
  final bool isDark;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 100,
      height: 100,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: isDark
            ? AppColors.darkSurfaceAlt
            : AppColors.lightSurfaceAlt,
        border: Border.all(
          color: isDark ? AppColors.darkBorder : AppColors.lightBorder,
          width: 1.5,
        ),
      ),
      child: Center(
        child: Text(
          'CB',
          style: TextStyle(
            fontSize: 36,
            fontWeight: FontWeight.w700,
            color: color,
            letterSpacing: 2,
          ),
        ),
      ),
    );
  }
}
