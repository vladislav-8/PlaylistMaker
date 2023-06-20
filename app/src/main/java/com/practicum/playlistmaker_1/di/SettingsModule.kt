package com.practicum.playlistmaker_1.di

import android.content.Context
import com.practicum.playlistmaker_1.App
import com.practicum.playlistmaker_1.settings.data.SettingsRepository
import com.practicum.playlistmaker_1.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker_1.settings.data.storage.SettingsThemeStorage
import com.practicum.playlistmaker_1.settings.data.storage.SharedPrefsThemeStorage
import com.practicum.playlistmaker_1.settings.domain.SettingsInteractor
import com.practicum.playlistmaker_1.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker_1.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val settingsModule = module {

    viewModel {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get(),
            androidApplication() as App
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(storage = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    single<SettingsThemeStorage> {
        SharedPrefsThemeStorage(sharedPreferences = get())
    }

    single {
        androidContext()
            .getSharedPreferences(
                "local_storage", Context.MODE_PRIVATE
            )
    }

}