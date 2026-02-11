package com.alex.ps.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alex.ps.data.poe.PoeShortagesDataSource
import com.alex.ps.data.poe.QueueListParser
import com.alex.ps.data.poe.ShortagesRepositoryImpl
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.data.settings.settingsDataStore
import com.alex.ps.domain.ShortagesDataSource
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.infrastructure.Notifier
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<DataStore<Preferences>>{
        androidContext().settingsDataStore
    }

    single {
        SettingsDataStore(dataStore = get())
    }

    single {
        QueueListParser()
    }

    single<ShortagesDataSource> {
        PoeShortagesDataSource(
            queueListParser = get()
        )
    }

    single<ShortagesRepository> {
        ShortagesRepositoryImpl(
            shortagesDataSource = get(),
            dataStore = get()
        )
    }

    single {
        Notifier(
            context = androidContext()
        )
    }
}