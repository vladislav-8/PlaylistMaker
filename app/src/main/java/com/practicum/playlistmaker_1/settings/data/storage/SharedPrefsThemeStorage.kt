package com.practicum.playlistmaker_1.settings.data.storage

import android.content.SharedPreferences
import com.practicum.playlistmaker_1.settings.domain.models.ThemeSettings

class SharedPrefsThemeStorage(private val sharedPreferences: SharedPreferences) : SettingsThemeStorage {

    override fun saveThemeSettings(settings: ThemeSettings) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME, settings.darkTheme)
            .apply()
    }

    override fun getThemeSettings(): ThemeSettings {
        val darkTheme = sharedPreferences.getBoolean(DARK_THEME, false)
        return ThemeSettings(darkTheme)
    }

    companion object {
        const val DARK_THEME = "dark_theme"
    }
}