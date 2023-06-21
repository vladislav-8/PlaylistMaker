package com.practicum.playlistmaker_1

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker_1.di.playerModule
import com.practicum.playlistmaker_1.di.searchModule
import com.practicum.playlistmaker_1.di.settingsModule
import com.practicum.playlistmaker_1.di.sharingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(playerModule, searchModule, settingsModule, sharingModule))
        }

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER_SHARED_PREFS = "playlist_maker_shared_prefs"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}