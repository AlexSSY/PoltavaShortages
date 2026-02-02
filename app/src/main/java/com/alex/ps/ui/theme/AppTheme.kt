package com.alex.ps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorScheme = darkColorScheme(
    background = BlackHole90,
    onBackground = WhiteCold100,
    primary = GreenBlue,
    onPrimary = BlackHole90,
    secondary = BlueDark,
    onSecondary = WhiteCold100,
    error = RedVirgin,
    onError = WhiteCold100,
    surface = BlackHole50,
    onSurface = WhiteCold100
)

private val lightColorScheme = lightColorScheme(
    background = WhiteCold100,
    onBackground = BlackHole90,
    primary = GreenBlue,
    onPrimary = WhiteCold100,
    secondary = BlueDark,
    onSecondary = WhiteCold100
)

private val typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 30.15.sp // 36px
    ),
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 16.75.sp // 20px
    ),
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 15.1.sp // 18px
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontSize = 13.4.sp // 16px
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.72.sp // 14px
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.05.sp // 12px
    )
)

private val shapes = Shapes(
    large = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(50)
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

object AppTheme {
    val colorScheme: ColorScheme
        @Composable get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable get() = MaterialTheme.typography

    val shape: Shapes
        @Composable get() = MaterialTheme.shapes
}