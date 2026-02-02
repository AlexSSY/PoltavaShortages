package com.alex.ps.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Рисует круговой индикатор прогресса в виде .
 *
 * @param modifier модификатор.
 * @param percent процент прогресса (от 0 до 100).
 * @param radius раиус
 * @param width ширина бара
 * @param backgroundColor цвет пространства между innerRadius и outerRadius
 * @param segmentColor цвет сегментов
 * @param segments к-во сегментов
 * @param gap зазор между сегментами
 */
@Composable
fun CircularSegmentProgressBar(
    modifier: Modifier = Modifier,
    percent: Float,
    radius: Dp,
    width: Dp,
    backgroundColor: Color,
    segmentColor: Color,
    segments: Int = 32,
    gap: Dp = 2.dp
) {
    Canvas(modifier = modifier.size((radius + width / 2) * 2)) {

        val center = Offset(size.width / 2, size.height / 2)

        val ringRadius = radius.toPx()
        val segmentThickness = width.toPx()
        val gapPx = gap.toPx()

        val circumference = 2 * Math.PI * ringRadius
        val segmentLength =
            (circumference - segments * gapPx) / segments

        val filledSegments =
            (segments * (percent.coerceIn(0f, 100f) / 100f)).toInt()

        // ---- фон кольца ----
        drawCircle(
            color = backgroundColor,
            radius = ringRadius,
            center = center,
            style = Stroke(width = segmentThickness)
        )

        for (i in 0 until filledSegments) {
            val angle = -0f + i * (360f / segments)

            withTransform({
                rotate(angle, center)
                translate(
                    left = center.x - segmentLength.toFloat() / 2,
                    top = center.y - ringRadius - segmentThickness / 2
                )
            }) {
                drawRoundRect(
                    color = segmentColor,
                    size = Size(segmentLength.toFloat(), segmentThickness),
                    cornerRadius = CornerRadius(
                        x = segmentThickness / 4,
                        y = segmentThickness / 4
                    )
                )
            }
        }
    }
}