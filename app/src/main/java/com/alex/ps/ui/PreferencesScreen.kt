package com.alex.ps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.alex.ps.domain.ThemeSetting
import com.alex.ps.domain.SettingsRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PreferencesScreen(
    onBack: () -> Unit
) {
    val settingsRepository: SettingsRepository = koinInject<SettingsRepository>()
    val currentSettings = settingsRepository.settingsFlow.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PreferencesScreenItem(name = "Theme") {
            Text(
                modifier = Modifier
                    .clickable(
                        onClick = {}
                    ),
                text = currentSettings.value.theme.name,
                textDecoration = TextDecoration.Underline
            )
        }
        PreferencesScreenItem(name = "Queue") {
            Button(onClick = {}) {
                Text(
                    text = "%d.%d".format(
                    currentSettings.value.selectedQueue.major,
                    currentSettings.value.selectedQueue.minor)
                )
            }
        }
        PreferencesScreenItem(name = "Language") {
            Button(onClick = {}) {
                Text(text = currentSettings.value.language.name)
            }
        }
    }
}

@Composable
fun PreferencesScreenItem(
    name: String = "Unnamed",
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        content()
    }
}