package com.alex.ps.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.ps.data.settings.Settings
import com.alex.ps.data.settings.SettingsDataStore

@Composable
fun SettingsDrawer(
    settingsDataStore: SettingsDataStore
) {
    val settings by settingsDataStore.settingsFlow
        .collectAsState(initial = Settings.default())

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Настройки",
            style = AppTheme.typography.titleLarge
        )

        ThemeSection(
            selectedTheme = settings.theme,
            onThemeSelected = {
                LaunchedEffect(it) {
                    settingsDataStore.setTheme(it)
                }
            }
        )

        LanguageSection(
            selectedLanguage = settings.language,
            onLanguageSelected = {
                LaunchedEffect(it) {
                    settingsDataStore.setLanguage(it)
                }
            }
        )
    }
}