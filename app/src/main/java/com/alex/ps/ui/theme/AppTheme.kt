package com.alex.ps.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// https://www.youtube.com/watch?v=eTVVT9cX4Bw

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
    titleNormal = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontSize = 12.sp
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(16.dp),
    button = RoundedCornerShape(50)
)

private val size = AppSize(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides rippleIndication,
        content = content
    )
}