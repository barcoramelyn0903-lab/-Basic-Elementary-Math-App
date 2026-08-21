package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JunglePrimaryLight,
    onPrimary = Color.Black,
    primaryContainer = JunglePrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = JungleSecondaryLight,
    onSecondary = Color.Black,
    secondaryContainer = JungleSecondaryDark,
    onSecondaryContainer = Color.White,
    tertiary = RiverBlueLight,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = JunglePrimary,
    onPrimary = Color.White,
    primaryContainer = CardBackgroundGreen,
    onPrimaryContainer = JunglePrimaryDark,
    secondary = JungleSecondaryDark,
    onSecondary = Color.White,
    secondaryContainer = CardBackgroundYellow,
    onSecondaryContainer = Color(0xFF5D4037),
    tertiary = RiverBlue,
    onTertiary = Color.White,
    background = JungleBackgroundLight,
    surface = JungleSurfaceLight,
    onBackground = JungleTextPrimary,
    onSurface = JungleTextPrimary,
    surfaceVariant = JungleSurfaceElevated,
    onSurfaceVariant = JungleTextSecondary
)

@Composable
fun JungleMathTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
