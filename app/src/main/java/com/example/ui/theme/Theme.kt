package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NexellaColorScheme = lightColorScheme(
    primary = NexellaPurple,
    onPrimary = NexellaWhite,
    primaryContainer = NexellaPurpleLight,
    onPrimaryContainer = NexellaPurpleDark,
    secondary = NexellaGold,
    onSecondary = NexellaWhite,
    secondaryContainer = NexellaGoldLight,
    onSecondaryContainer = NexellaGold,
    background = NexellaBackground,
    onBackground = NexellaTextDark,
    surface = NexellaSurface,
    onSurface = NexellaTextDark,
    surfaceVariant = NexellaBackground,
    onSurfaceVariant = NexellaSubtext,
    outline = NexellaBorder
)

@Composable
fun NexellaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NexellaColorScheme,
        typography = Typography,
        content = content
    )
}

