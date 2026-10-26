package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Sensi App Cyber Gaming Palette
val CyberBackground = Color(0xFF070A19)
val CyberSurface = Color(0xFF0F182D)
val CyberSurfaceVariant = Color(0xFF16223F)
val CyberBorder = Color(0xFF233560)

val NeonCyan = Color(0xFF00EBFF)
val NeonPurple = Color(0xFF8C3CFF)
val NeonOrange = Color(0xFFFF5E3A)
val NeonGreen = Color(0xFF00FF9D)
val NeonYellow = Color(0xFFFFD600)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA5B4CB)
val TextMuted = Color(0xFF6B7D9E)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = NeonCyan,
    secondary = NeonPurple,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF381E72),
    onSecondaryContainer = Color(0xFFE8DDFF),
    tertiary = NeonOrange,
    onTertiary = Color(0xFFFFFFFF),
    background = CyberBackground,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
)
