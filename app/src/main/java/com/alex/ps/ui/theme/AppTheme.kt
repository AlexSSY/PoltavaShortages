package com.alex.ps.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val darkColorScheme = AppColorScheme(
    background = BlackHole90,
    onBackground = WhiteCold100,
    primary = GreenBlue,
    onPrimary = WhiteCold100,
    secondary = BlueDark,
    onSecondary = WhiteCold100,
)

private val lightColorScheme = AppColorScheme(
    background = WhiteCold100,
    onBackground = BlackHole90,
    primary = GreenBlue,
    onPrimary = WhiteCold100,
    secondary = BlueDark,
    onSecondary = WhiteCold100
)

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleNormal = TODO(), //https://www.youtube.com/watch?v=eTVVT9cX4Bw
    body = TODO(),
    labelSmall = TODO()
)