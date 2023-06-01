package com.practicum.playlistmaker_1.creator

import com.practicum.playlistmaker_1.data.repository.TrackPlayerImpl
import com.practicum.playlistmaker_1.domain.models.Track
import com.practicum.playlistmaker_1.domain.repository.PlayerInteractorImpl
import com.practicum.playlistmaker_1.presentation.player.PlayerView
import com.practicum.playlistmaker_1.presentation.player.presenter.PlayerPresenter

object Creator {

    fun providePresenter(view: PlayerView, track: Track): PlayerPresenter {
        val interactor = PlayerInteractorImpl(trackPlayer = TrackPlayerImpl(track.previewUrl))
        return PlayerPresenter(
            view = view,
            interactor = interactor,
        )
    }
}