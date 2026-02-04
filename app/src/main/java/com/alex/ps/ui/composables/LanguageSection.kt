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
import com.alex.ps.data.settings.AppLanguage
import com.alex.ps.ui.theme.AppTheme

@Composable
fun LanguageSection(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    Column {
        Text(
            "Язык",
            style = AppTheme.typography.titleMedium
        )

        AppLanguage.values().forEach { language ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLanguageSelected(language) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedLanguage == language,
                    onClick = { onLanguageSelected(language) }
                )
                Text(
                    text = language.name,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}