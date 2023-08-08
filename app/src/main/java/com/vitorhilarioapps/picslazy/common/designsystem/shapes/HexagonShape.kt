package com.vitorhilarioapps.picslazy.common.designsystem.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

val HexagonShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val minSize = min(size.width, size.height)
        val angleRadians = Math.toRadians(60.0).toFloat()
        val radius = minSize / 2f
        return Outline.Generic(
            Path().apply {
                (0..5).forEach { i ->
                    val currentAngle = angleRadians * i
                    val x = radius + radius * cos(currentAngle)
                    val y = radius + radius * sin(currentAngle)
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }
        )
    }
}

fun itemOverlap(density: Density, size: Dp): Dp {
    return with(density) {
        val sizeInPx = size.toPx()
        val radius = sizeInPx / 2f
        val y = radius + radius * sin(Math.toRadians(240.0))
        (- radius - y).toFloat().toDp()
    }
}