package com.itsbenzopila.drinkshop.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = BrandOnPrimary,
    secondary = BrandSecondary,
    background = BrandBackground,
    surface = BrandSurface,
    onBackground = BrandOnBackground,
    error = BrandError,
)

private val DarkColors = darkColorScheme(
    primary = BrandPrimaryLight,
    onPrimary = BrandOnPrimary,
    secondary = BrandSecondary,
)

@Composable
fun DrinkshopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content,
    )
}
