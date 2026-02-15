package com.alex.ps.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun SummaryDonutChart(
    modifier: Modifier = Modifier,
    negativeValue: Float,
    positiveValue: Float,
    date: String
) {
    val total = abs(negativeValue) + positiveValue
    val negativeSweep = (abs(negativeValue) / total) * 360f
    val positiveSweep = (positiveValue / total) * 360f

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier
                .size(260.dp)
        ) {

            val strokeWidth = 40.dp.toPx()
            val diameter = size.minDimension
            val topLeft = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )
            val arcSize = Size(diameter, diameter)

            // Фон
            drawArc(
                color = Color(0xFF1E1E1E),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                size = arcSize,
                topLeft = topLeft
            )

            // Красный сегмент
            drawArc(
                color = Color(0xFF7B3F3F),
                startAngle = -90f,
                sweepAngle = negativeSweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                size = arcSize,
                topLeft = topLeft
            )

            // Зелёный сегмент
            drawArc(
                color = Color(0xFF2E7D32),
                startAngle = -90f + negativeSweep,
                sweepAngle = positiveSweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                size = arcSize,
                topLeft = topLeft
            )
        }

        // Текст по центру
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Row {
                Text(
                    text = String.format("%.1f", negativeValue),
                    color = Color.Red,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+" + String.format("%.1f", positiveValue),
                    color = Color.Green,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = date,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
