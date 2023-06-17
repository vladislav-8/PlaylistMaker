package com.practicum.playlistmaker_1.settings.domain

import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}