package com.alex.ps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.alex.ps.R
import com.alex.ps.domain.ThemeSetting
import com.alex.ps.domain.SettingsRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PreferencesScreen(
    onThemeClick: () -> Unit,
    onQueueClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val settingsRepository: SettingsRepository = koinInject<SettingsRepository>()
    val currentSettings = settingsRepository.settingsFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PreferencesScreenItem(
            name = "Theme",
            value = currentSettings.value.theme.name,
            onClick = onThemeClick
        )
        PreferencesScreenItem(
            name = stringResource(id = R.string.queue_setting),
            value = "%d.%d".format(
                currentSettings.value.selectedQueue.major,
                currentSettings.value.selectedQueue.minor
            ),
            onClick = onQueueClick
        )
        PreferencesScreenItem(
            name = "Language",
            value = currentSettings.value.language.name,
            onClick = onLanguageClick
        )
    }
}

@Composable
fun PreferencesScreenItem(
    name: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable(
                onClick = onClick
            )
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}