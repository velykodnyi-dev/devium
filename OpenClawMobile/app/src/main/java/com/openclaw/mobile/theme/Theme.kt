package com.openclaw.mobile.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

private val IdeColorScheme = darkColorScheme(
    primary = IdeAccent,
    secondary = IdeInfo,
    tertiary = IdeSurfaceLight,
    background = IdeBackground,
    surface = IdeSurface,
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = IdeTextPrimary,
    onBackground = IdeTextPrimary,
    onSurface = IdeTextPrimary,
    error = IdeError,
    surfaceVariant = IdeSurfaceLight,
    onSurfaceVariant = IdeTextSecondary
)

@Composable
fun OpenClawTheme(
    darkTheme: Boolean = true, // We force the IDE Dark Theme
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = IdeBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = IdeColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
