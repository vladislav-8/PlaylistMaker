package com.practicum.playlistmaker_1.settings.data.impl

import com.practicum.playlistmaker_1.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker_1.settings.data.storage.SettingsThemeStorage
import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

class SettingsRepositoryImpl(private val storage: SettingsThemeStorage): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return storage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveThemeSettings(settings)
    }

}