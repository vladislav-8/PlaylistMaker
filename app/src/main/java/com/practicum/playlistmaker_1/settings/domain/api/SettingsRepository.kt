package com.practicum.playlistmaker_1.settings.domain.api

import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}