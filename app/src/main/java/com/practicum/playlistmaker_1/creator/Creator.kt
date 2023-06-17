package com.practicum.playlistmaker_1.creator

import android.content.Context
import com.practicum.playlistmaker_1.data.repository.TrackPlayerImpl
import com.practicum.playlistmaker_1.domain.models.Track
import com.practicum.playlistmaker_1.domain.repository.PlayerInteractorImpl
import com.practicum.playlistmaker_1.player.PlayerView
import com.practicum.playlistmaker_1.player.presenter.PlayerPresenter
import com.practicum.playlistmaker_1.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker_1.settings.data.storage.SharedPrefsThemeStorage
import com.practicum.playlistmaker_1.settings.domain.SettingsInteractor
import com.practicum.playlistmaker_1.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker_1.sharing.SharingInteractor
import com.practicum.playlistmaker_1.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker_1.sharing.domain.impl.SharingInteractorImpl

object Creator {

    fun providePresenter(view: PlayerView, track: Track): PlayerPresenter {
        val interactor = PlayerInteractorImpl(trackPlayer = TrackPlayerImpl(track.previewUrl))
        return PlayerPresenter(
            view = view,
            interactor = interactor,
        )
    }

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
}