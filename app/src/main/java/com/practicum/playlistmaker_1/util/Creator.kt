package com.practicum.playlistmaker_1.util

import android.content.Context
import com.practicum.playlistmaker_1.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker_1.player.domain.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker_1.search.data.impl.SearchRepositoryImpl
import com.practicum.playlistmaker_1.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_1.search.data.storage.SharedPrefsHistoryStorage
import com.practicum.playlistmaker_1.search.domain.SearchInteractor
import com.practicum.playlistmaker_1.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker_1.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker_1.settings.data.storage.SharedPrefsThemeStorage
import com.practicum.playlistmaker_1.settings.domain.SettingsInteractor
import com.practicum.playlistmaker_1.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker_1.sharing.SharingInteractor
import com.practicum.playlistmaker_1.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker_1.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val sharedPrefs =
            context.getSharedPreferences(SharedPrefsThemeStorage.DARK_THEME, Context.MODE_PRIVATE)
        val repository = SettingsRepositoryImpl(SharedPrefsThemeStorage(sharedPrefs))
        return SettingsInteractorImpl(repository)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        val externalNavigator = ExternalNavigatorImpl(context)
        return SharingInteractorImpl(externalNavigator)
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        val sharedPrefs = context.getSharedPreferences(SharedPrefsHistoryStorage.TRACK_SEARCH_HISTORY, Context.MODE_PRIVATE)
        return SearchInteractorImpl(
            SearchRepositoryImpl(RetrofitNetworkClient(), SharedPrefsHistoryStorage(sharedPrefs))
        )
    }

    fun providePlayerInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl(trackUrl))
    }
}