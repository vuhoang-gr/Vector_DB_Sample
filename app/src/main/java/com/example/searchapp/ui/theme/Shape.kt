package com.example.searchapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


/**
 * A class to model common shapes
 */
@Immutable
@Suppress("MagicNumber")
internal data class Shape(
    val roundedCornerSmall: RoundedCornerShape = RoundedCornerShape(8.dp),
    val roundedCornerMedium: RoundedCornerShape = RoundedCornerShape(15.dp),
    val roundedCornerLarge: RoundedCornerShape = RoundedCornerShape(18.dp),
    val roundedCornerTop: RoundedCornerShape = RoundedCornerShape(
        topStart = 15.dp,
        topEnd = 15.dp,
    ),
    val dashedStroke: DrawStyle = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(10f, 10f),
            phase = 0f
        )
    ),
    val cornerRadius: CornerRadius = CornerRadius(x = 8f, y = 8f)
)

/**
 * A composition local for [Shape].
 */
internal val LocalShape = compositionLocalOf { Shape() }

internal val MaterialTheme.shape: Shape
    @Composable
    @ReadOnlyComposable
    get() = LocalShape.current


