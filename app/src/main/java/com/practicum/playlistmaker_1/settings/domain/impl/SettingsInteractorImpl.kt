package com.practicum.playlistmaker_1.settings.domain.impl

import com.practicum.playlistmaker_1.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker_1.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}