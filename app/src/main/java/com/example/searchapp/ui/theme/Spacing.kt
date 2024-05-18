package com.example.searchapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
internal data class Spacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 5.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 15.dp,
    val large: Dp = 21.dp,
)

/**
 * A composition local for [Spacing].
 */
internal val LocalSpacing = compositionLocalOf { Spacing() }

internal val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current



