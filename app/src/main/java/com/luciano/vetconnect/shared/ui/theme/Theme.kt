// Theme.kt
package com.luciano.vetconnect.shared.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandColors.Primary,
    secondary = BrandColors.Secondary,
    tertiary = BrandColors.Tertiary,
    background = BackgroundColors.Primary,
    surface = BackgroundColors.Surface,
    onPrimary = TextColors.OnDark,
    onSecondary = TextColors.Primary,
    onTertiary = TextColors.Primary,
    onBackground = TextColors.Primary,
    onSurface = TextColors.Primary,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandColors.Primary,
    secondary = BrandColors.Secondary,
    tertiary = BrandColors.Tertiary,
    background = NeutralColors.Gray3,
    surface = NeutralColors.Gray3,
    onPrimary = TextColors.OnDark,
    onSecondary = TextColors.OnDark,
    onTertiary = TextColors.OnDark,
    onBackground = TextColors.OnDark,
    onSurface = TextColors.OnDark,
)

@Composable
fun VetConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// Definición de formas personalizadas para la aplicación
val Shapes = Shapes(
    extraSmall = ShapeDefaults.ExtraSmall,
    small = ShapeDefaults.Small,
    medium = ShapeDefaults.Medium,
    large = ShapeDefaults.Large,
    extraLarge = ShapeDefaults.ExtraLarge
)