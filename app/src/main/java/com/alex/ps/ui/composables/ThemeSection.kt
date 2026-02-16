package com.alex.ps.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.ps.domain.ThemeSetting

@Composable
fun ThemeSection(
    selectedTheme: ThemeSetting,
    onThemeSelected: (ThemeSetting) -> Unit
) {
    Column {
        Text(
            "Тема",
            style = com.alex.ps.ui.theme.AppTheme.typography.titleMedium
        )

        ThemeSetting.values().forEach { theme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(theme) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = { onThemeSelected(theme) }
                )
                Text(
                    text = theme.name,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}