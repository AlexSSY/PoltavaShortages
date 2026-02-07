package com.alex.ps.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alex.ps.data.poe.LocalRepository
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.data.settings.settingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<DataStore<Preferences>>{
        androidContext().settingsDataStore
    }

    single {
        SettingsDataStore(dataStore = get())
    }

    single<LocalRepository> {
        LocalRepository(dataStore = get())
    }
}