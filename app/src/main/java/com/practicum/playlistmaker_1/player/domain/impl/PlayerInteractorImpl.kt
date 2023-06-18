package com.practicum.playlistmaker_1.player.domain.impl

import com.practicum.playlistmaker_1.player.domain.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.PlayerRepository
import com.practicum.playlistmaker_1.player.ui.models.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {

    override fun preparePlayer() {
        repository.prepare()
    }
    override fun startPlayer() {
        repository.start()
    }

    override fun pausePlayer() {
        repository.pause()
    }

    override fun stopPlayer() {
        repository.release()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun getPlayerState(): PlayerState {
        return repository.playerState
    }

    override fun setTrackCompletionListener(listener: (() -> Unit)) {
        repository.completionListener = listener
    }
}