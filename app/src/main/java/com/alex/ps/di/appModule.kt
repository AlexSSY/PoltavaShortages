package com.alex.ps.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alex.ps.data.PoeShortagesDataSource
import com.alex.ps.data.QueueListParser
import com.alex.ps.data.ShortagesRepositoryImpl
import com.alex.ps.data.SettingsRepositoryImpl
import com.alex.ps.data.TimeProviderImpl
import com.alex.ps.data.settingsDataStore
import com.alex.ps.domain.QueueProvider
import com.alex.ps.domain.SettingsRepository
import com.alex.ps.domain.ShortagesDataSource
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.TimePeriodsWithStateUseCase
import com.alex.ps.domain.TimeProvider
import com.alex.ps.infrastructure.Notifier
import com.alex.ps.ui.HomeViewModel
import com.alex.ps.ui.TomorrowViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope>(named("AppScope")) {
        CoroutineScope(
            SupervisorJob() + Dispatchers.Default + CoroutineName("AppScope")
        )
    }

    single<DataStore<Preferences>> {
        androidContext().settingsDataStore
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(dataStore = get())
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
            dataStore = get(),
            scope = get(named("AppScope"))
        )
    }

    single<TimeProvider> {
        TimeProviderImpl(
            scope = get(named("AppScope"))
        )
    }

    single {
        QueueProvider(
            shortagesRepository = get(),
            settingsRepository = get(),
            scope = get(named("AppScope"))
        )
    }

    viewModel {
        HomeViewModel(
            shortagesRepository = get(),
            timeProvider = get(),
            queueProvider = get()
        )
    }

    viewModel {
        TomorrowViewModel(
            queueProvider = get(),
            timeProvider = get()
        )
    }

    single {
        Notifier(
            context = androidContext()
        )
    }
}