package com.alex.ps.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Рисует круговой индикатор прогресса в виде .
 *
 * @param modifier модификатор.
 * @param percent процент прогресса (от 0 до 100).
 * @param radius раиус
 * @param width ширина бара
 * @param backgroundColor цвет пространства между innerRadius и outerRadius
 * @param barsColor цыет сегментов (баров)
 * @param segments к-во сегментов (баров)
 * @param gapAngle зазор между сегментами
 */
@Composable
fun CircularSegmentProgressBar(
    modifier: Modifier = Modifier,
    percent: Int,
    radius: Dp,
    width: Dp,
    backgroundColor: Color,
    barsColor: Color,
    segments: Int = 10,
    gapAngle: Float = 4f // небольшой зазор между сегментами
) {
    val clampedPercent = percent.coerceIn(0, 100)

    Canvas(modifier = modifier.size(radius * 2)) {
        val center = size.center
        val radiusPx = radius.toPx()
        val widthPx = width.toPx()

        // Фон — цельное кольцо
        drawArc(
            color = backgroundColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = widthPx,
                cap = StrokeCap.Round
            )
        )

        val sweepPerSegment = (360f - segments * gapAngle) / segments
        val activeSegments = ((clampedPercent / 100f) * segments).toInt()

        for (i in 0 until activeSegments) {
            val startAngle = -90f + i * (sweepPerSegment + gapAngle)
            drawArc(
                color = barsColor,
                startAngle = startAngle + gapAngle / 2,
                sweepAngle = sweepPerSegment - gapAngle,
                useCenter = false,
                style = Stroke(
                    width = widthPx,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}