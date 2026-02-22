package com.alex.ps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alex.ps.domain.LanguageSetting
import com.alex.ps.domain.SettingsRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    onLanguageSelected: () -> Unit
) {
    val settingsRepository: SettingsRepository = koinInject<SettingsRepository>()
    val currentSettings = settingsRepository.settingsFlow.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LanguageSetting.entries.forEach { entry ->
            val bgColor = if (
                entry == currentSettings.value.language
            )
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surface

            Row(
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable(
                        onClick = {
                            scope.launch {
                                settingsRepository.setLanguage(entry)
                                onLanguageSelected()
                            }
                        }
                    )
                    .padding(vertical = 14.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = entry.name)
            }
        }
    }
}