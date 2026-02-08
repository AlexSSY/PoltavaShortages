package com.alex.ps.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.alex.ps.data.poe.ShortagesRepositoryImpl
import com.alex.ps.data.settings.SettingsDataStore

class HomeViewModel(
    settingsDataStore: SettingsDataStore,
    shortagesRepositoryImpl: ShortagesRepositoryImpl
): ViewModel() {
    
}