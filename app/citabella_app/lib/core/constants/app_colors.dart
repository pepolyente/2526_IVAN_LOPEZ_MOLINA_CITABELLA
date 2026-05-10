// Paleta de colores de CitaBella Flutter Shell.
// Basada en los design tokens del frontend Angular (src/styles/tokens.css).
//
// LIGHT THEME → Navy (#0A0F1E) sobre fondo azul claro (#E4F0F6)
// DARK THEME  → Gold (#C8A96E) sobre fondo casi negro (#0D0D0D)

import 'package:flutter/material.dart';

class AppColors {
  AppColors._();

  // ── Light Theme ───────────────────────────────────────────────────────────
  static const Color lightBackground   = Color(0xFFE4F0F6); // --color-bg
  static const Color lightSurface      = Color(0xFFFFFFFF); // --color-surface
  static const Color lightPrimary      = Color(0xFF0A0F1E); // --color-primary
  static const Color lightPrimaryLight = Color(0xFFC8DDE8); // --color-primary-light
  static const Color lightTextMuted    = Color(0xFF52697A); // --color-text-muted
  static const Color lightBorder       = Color(0xFFB8D4E2); // --color-border
  static const Color lightSurfaceAlt   = Color(0xFFF0F7FA); // --color-surface-alt

  // ── Dark Theme ────────────────────────────────────────────────────────────
  static const Color darkBackground    = Color(0xFF0D0D0D); // --color-bg dark
  static const Color darkSurface       = Color(0xFF1A1A1A); // --color-surface dark
  static const Color darkPrimary       = Color(0xFFC8A96E); // --color-primary dark (gold)
  static const Color darkPrimaryDark   = Color(0xFFB8914A); // --color-primary-dark dark
  static const Color darkTextMuted     = Color(0xFF8A8076); // --color-text-muted dark
  static const Color darkBorder        = Color(0xFF2E2E2E); // --color-border dark
  static const Color darkSurfaceAlt    = Color(0xFF222222); // --color-surface-alt dark

  // ── Estado / Semánticos ───────────────────────────────────────────────────
  static const Color danger            = Color(0xFFDC2626); // --color-danger light
  static const Color dangerDark        = Color(0xFFF87171); // --color-danger dark
  static const Color success           = Color(0xFF16A34A); // --color-success light
  static const Color successDark       = Color(0xFF4ADE80); // --color-success dark

  // ── Utilidad ──────────────────────────────────────────────────────────────
  static const Color transparent       = Colors.transparent;
  static const Color white             = Colors.white;
  static const Color black             = Colors.black;

  // ── Progress bar ─────────────────────────────────────────────────────────
  // Color de la barra de carga — se adapta al tema
  static Color progressBarColor(Brightness brightness) {
    return brightness == Brightness.dark ? darkPrimary : lightPrimary;
  }

  // ── Background del loading overlay ───────────────────────────────────────
  static Color loadingBackground(Brightness brightness) {
    return brightness == Brightness.dark ? darkBackground : lightBackground;
  }
}
