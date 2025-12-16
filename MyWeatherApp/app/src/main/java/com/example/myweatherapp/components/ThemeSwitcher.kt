package com.example.myweatherapp.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable

/**
 * A switch component that toggles between light and dark theme.
 *
 * @param darkTheme A boolean indicating whether dark theme is enabled.
 * @param onThemeChange A callback function to toggle theme between light and dark.
 */
@Composable
fun ThemeSwitcher(darkTheme: Boolean, onThemeChange: () -> Unit) {
    Switch(
        checked = darkTheme,
        onCheckedChange = {
            onThemeChange() // when switch is pressed, calls this lambda: { darkTheme = !darkTheme }
        }
    )
}
