package com.practicum.playlistmaker_1.settings.data.storage

import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

interface SettingsThemeStorage {
    fun saveThemeSettings(settings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
}