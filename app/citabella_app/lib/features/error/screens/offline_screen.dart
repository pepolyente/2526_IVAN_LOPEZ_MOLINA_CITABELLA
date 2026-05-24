// OfflineScreen — Pantalla de error para dos casos:
//   1. Sin internet      → [OfflineType.noNetwork]
//   2. Servidor caído    → [OfflineType.serverError]
//
// Muestra:
//   • Icono descriptivo
//   • Título y descripción del error
//   • Botón "Reintentar" que llama al callback onRetry
//
// El callback onRetry es proporcionado por WebViewScreen,
// que delega en WebViewStateController.retry().

import 'package:flutter/material.dart';
import '../../../core/config/app_config.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/constants/app_strings.dart';
import '../../webview/controllers/webview_state_controller.dart';

class OfflineScreen extends StatelessWidget {
  const OfflineScreen({
    super.key,
    required this.status,
    required this.onRetry,
  });

  final WebViewStatus status;
  final VoidCallback onRetry;

  @override
  Widget build(BuildContext context) {
    final brightness = Theme.of(context).brightness;
    final isDark = brightness == Brightness.dark;

    final bgColor      = isDark ? AppColors.darkBackground   : AppColors.lightBackground;
    final surfaceColor = isDark ? AppColors.darkSurface      : AppColors.lightSurface;
    final primaryColor = isDark ? AppColors.darkPrimary      : AppColors.lightPrimary;
    final mutedColor   = isDark ? AppColors.darkTextMuted    : AppColors.lightTextMuted;
    final borderColor  = isDark ? AppColors.darkBorder       : AppColors.lightBorder;
    final textColor    = isDark ? const Color(0xFFF0EAD6)    : AppColors.lightPrimary;

    final isServerError = status == WebViewStatus.serverError;

    return Scaffold(
      backgroundColor: bgColor,
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // ── Icono ──────────────────────────────────────────────────
                Container(
                  width: 96,
                  height: 96,
                  decoration: BoxDecoration(
                    color: surfaceColor,
                    shape: BoxShape.circle,
                    border: Border.all(color: borderColor, width: 1.5),
                  ),
                  child: Icon(
                    isServerError
                        ? Icons.dns_outlined
                        : Icons.wifi_off_rounded,
                    size: 44,
                    color: mutedColor,
                  ),
                ),

                const SizedBox(height: 28),

                // ── Título ─────────────────────────────────────────────────
                Text(
                  isServerError
                      ? AppStrings.offlineTitle
                      : AppStrings.noInternetTitle,
                  style: TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.w700,
                    color: textColor,
                    height: 1.3,
                  ),
                  textAlign: TextAlign.center,
                ),

                const SizedBox(height: 14),

                // ── Descripción ───────────────────────────────────────────
                Text(
                  isServerError
                      ? AppStrings.offlineDescription
                      : AppStrings.noInternetDescription,
                  style: TextStyle(
                    fontSize: 14,
                    color: mutedColor,
                    height: 1.6,
                  ),
                  textAlign: TextAlign.center,
                ),

                const SizedBox(height: 40),

                // ── Botón Reintentar ──────────────────────────────────────
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    onPressed: onRetry,
                    icon: const Icon(Icons.refresh_rounded, size: 20),
                    label: const Text(AppStrings.retryButton),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: primaryColor,
                      foregroundColor:
                          isDark ? AppColors.darkBackground : AppColors.white,
                      padding: const EdgeInsets.symmetric(vertical: 14),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                      textStyle: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                      ),
                      elevation: 0,
                    ),
                  ),
                ),

                const SizedBox(height: 16),

                // ── URL de destino (ayuda al desarrollador) ───────────────
                Text(
                  'Servidor: ${AppConfig.baseUrl}',
                  style: TextStyle(
                    fontSize: 11,
                    color: mutedColor.withValues(alpha: 0.6),
                    fontFamily: 'monospace',
                  ),
                  textAlign: TextAlign.center,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

}
