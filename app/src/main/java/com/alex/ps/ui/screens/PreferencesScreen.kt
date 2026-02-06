package com.alex.ps.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alex.ps.data.settings.AppTheme
import com.alex.ps.data.settings.SettingsDataStore
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PreferencesScreen(
    onBack: () -> Unit
) {
    val settingsDataStore: SettingsDataStore = koinInject<SettingsDataStore>()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Preferences Screen")
        Button(onClick = {
            scope.launch {
                settingsDataStore.setTheme(AppTheme.SYSTEM)
            }
        }) {
            Text(text = "System")
        }
        Button(onClick = {
            scope.launch {
                settingsDataStore.setTheme(AppTheme.DARK)
            }
        }) {
            Text(text = "Dark")
        }
        Button(onClick = {
            scope.launch {
                settingsDataStore.setTheme(AppTheme.LIGHT)
            }
        }) {
            Text(text = "Light")
        }
        Button(onClick = onBack) {
            Text(text = "back")
        }
    }
}