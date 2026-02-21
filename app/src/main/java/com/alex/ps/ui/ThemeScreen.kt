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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alex.ps.domain.SettingsRepository
import com.alex.ps.domain.ThemeSetting
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ThemeScreen(
    modifier: Modifier = Modifier,
    onThemeSelected: () -> Unit
) {
    val settingsRepository: SettingsRepository = koinInject<SettingsRepository>()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ThemeSetting.entries.forEach { entry ->
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable(
                        onClick = {
                            scope.launch {
                                settingsRepository.setTheme(entry)
                                onThemeSelected()
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