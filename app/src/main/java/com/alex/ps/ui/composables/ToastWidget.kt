package com.alex.ps.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.theme.AppTheme

enum class ToastType {
    SUCCESS,
    WARNING,
    ERROR
}

@Composable
fun ToastWidget(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.ERROR,
    text: String
) {
    val backgroundColor = when (toastType) {
        ToastType.SUCCESS -> AppTheme.colorScheme.primary
        ToastType.WARNING -> AppTheme.colorScheme.tertiary
        ToastType.ERROR -> AppTheme.colorScheme.error
    }

    val textColor = when (toastType) {
        ToastType.SUCCESS -> AppTheme.colorScheme.onPrimary
        ToastType.WARNING -> AppTheme.colorScheme.onTertiary
        ToastType.ERROR -> AppTheme.colorScheme.onError
    }

    Text(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = AppTheme.shape.medium
            )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp),
        text = text,
        style = AppTheme.typography.labelMedium,
        color = textColor
    )
}