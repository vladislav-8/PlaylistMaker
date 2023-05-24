package com.practicum.playlistmaker_1.domain.repository

import com.practicum.playlistmaker_1.domain.api.PlayerInteractor
import com.practicum.playlistmaker_1.domain.api.TrackPlayer

class PlayerInteractorImpl(private val trackPlayer: TrackPlayer): PlayerInteractor {

    override fun preparePlayer() {
        trackPlayer.prepare()
    }
    override fun startPlayer() {
        trackPlayer.start()
    }

    override fun pausePlayer() {
        trackPlayer.pause()
    }

    override fun stopPlayer() {
        trackPlayer.release()
    }

    override fun getCurrentPosition() = trackPlayer.getCurrentPosition()

    override fun getPlayerState() = trackPlayer.playerState

    override fun setTrackCompletionListener(listener: (() -> Unit)) {
        trackPlayer.completionListener = listener
    }

}