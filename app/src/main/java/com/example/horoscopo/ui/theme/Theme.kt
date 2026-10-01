package com.example.horoscopo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF5C4D7D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = Color(0xFF1E1235),
    secondary = Color(0xFF326B5B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB7F1DC),
    onSecondaryContainer = Color(0xFF082018),
    tertiary = Color(0xFF8B4B36),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDBCF),
    onTertiaryContainer = Color(0xFF351005),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1E1B20),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1E1B20),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD5BBFF),
    onPrimary = Color(0xFF2D1D4B),
    primaryContainer = Color(0xFF443565),
    onPrimaryContainer = Color(0xFFE9DDFF),
    secondary = Color(0xFF9CD5C1),
    onSecondary = Color(0xFF12372C),
    secondaryContainer = Color(0xFF214E41),
    onSecondaryContainer = Color(0xFFB7F1DC),
    tertiary = Color(0xFFFFB59F),
    onTertiary = Color(0xFF52200F),
    tertiaryContainer = Color(0xFF6F3524),
    onTertiaryContainer = Color(0xFFFFDBCF),
    background = Color(0xFF151218),
    onBackground = Color(0xFFE8E0EA),
    surface = Color(0xFF151218),
    onSurface = Color(0xFFE8E0EA),
)

@Composable
fun HoroscopoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
