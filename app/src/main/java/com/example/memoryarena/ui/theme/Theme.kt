package com.example.memoryarena.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ArenaPrimary,
    onPrimary = ArenaOnPrimary,
    primaryContainer = ArenaPrimaryContainer,
    secondary = ArenaSecondary,
    onSecondary = ArenaOnSecondary,
    secondaryContainer = ArenaSecondaryContainer,
    tertiary = ArenaTertiary,
    onTertiary = ArenaOnTertiary,
    tertiaryContainer = ArenaTertiaryContainer,
    background = ArenaSurface,
    surface = ArenaSurface,
    onSurface = ArenaOnSurface,
    onSurfaceVariant = ArenaOnSurfaceVariant,
    outline = ArenaOutline,
    outlineVariant = ArenaOutlineVariant,
    surfaceContainer = ArenaSurfaceContainer,
    surfaceContainerHigh = ArenaSurfaceHigh,
    surfaceContainerHighest = ArenaSurfaceHighest,
    surfaceContainerLow = ArenaSurfaceLow,
    surfaceContainerLowest = ArenaSurfaceLowest
)

@Composable
fun MemoryArenaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
