package com.narrative.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NarrativeDarkScheme = darkColorScheme(
    primary          = Primary,
    onPrimary        = Color.White,
    primaryContainer = PrimaryDim,
    secondary        = Accent,
    onSecondary      = Color.Black,
    background       = Background,
    onBackground     = TextPrimary,
    surface          = Surface,
    onSurface        = TextPrimary,
    surfaceVariant   = SurfaceCard,
    onSurfaceVariant = TextMuted,
    outline          = Border,
    error            = Danger,
)

@Composable
fun NarrativeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NarrativeDarkScheme,
        typography  = NarrativeTypography,
        content     = content,
    )
}
