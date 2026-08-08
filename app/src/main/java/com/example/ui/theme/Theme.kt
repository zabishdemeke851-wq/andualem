package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EthioDarkPrimary,
    onPrimary = EthioDarkOnPrimary,
    primaryContainer = EthioDarkPrimaryContainer,
    onPrimaryContainer = EthioDarkOnPrimaryContainer,
    secondary = EthioDarkSecondary,
    onSecondary = EthioDarkOnSecondary,
    secondaryContainer = EthioDarkSecondaryContainer,
    onSecondaryContainer = EthioDarkOnSecondaryContainer,
    tertiary = EthioDarkTertiary,
    onTertiary = EthioDarkOnTertiary,
    tertiaryContainer = EthioDarkTertiaryContainer,
    onTertiaryContainer = EthioDarkOnTertiaryContainer,
    background = EthioDarkBackground,
    onBackground = EthioDarkOnSurface,
    surface = EthioDarkSurface,
    onSurface = EthioDarkOnSurface,
    surfaceVariant = EthioDarkSurfaceVariant,
    onSurfaceVariant = EthioDarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = EthioPrimary,
    onPrimary = EthioOnPrimary,
    primaryContainer = EthioPrimaryContainer,
    onPrimaryContainer = EthioOnPrimaryContainer,
    secondary = EthioSecondary,
    onSecondary = EthioOnSecondary,
    secondaryContainer = EthioSecondaryContainer,
    onSecondaryContainer = EthioOnSecondaryContainer,
    tertiary = EthioTertiary,
    onTertiary = EthioOnTertiary,
    tertiaryContainer = EthioTertiaryContainer,
    onTertiaryContainer = EthioOnTertiaryContainer,
    background = EthioBackground,
    onBackground = EthioOnSurface,
    surface = EthioSurface,
    onSurface = EthioOnSurface,
    surfaceVariant = EthioSurfaceVariant,
    onSurfaceVariant = EthioOnSurfaceVariant
)

@Composable
fun AndualemTheme(
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
