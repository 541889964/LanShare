package com.lanshare.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEF0FF),
    secondary = Color(0xFF7C73FF),
    background = Color(0xFFF7F8FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFF0F1F8),
    onSurface = Color(0xFF161A23),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE4E6EF),
    error = Color(0xFFE11D48),
)

private val Dark = darkColorScheme(
    primary = Color(0xFF7C73FF),
    onPrimary = Color(0xFF0E1014),
    primaryContainer = Color(0xFF22223A),
    secondary = Color(0xFFA79DFF),
    background = Color(0xFF0E1014),
    surface = Color(0xFF171A21),
    surfaceVariant = Color(0xFF22223A),
    onSurface = Color(0xFFE9EBF1),
    onSurfaceVariant = Color(0xFF9AA1B1),
    outline = Color(0xFF252932),
    error = Color(0xFFFF5C7C),
)

@Composable
fun LanShareTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(colorScheme = if (dark) Dark else Light, content = content)
}
