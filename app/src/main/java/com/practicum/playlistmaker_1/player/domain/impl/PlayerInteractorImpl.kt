package com.practicum.playlistmaker_1.player.domain.impl

import com.practicum.playlistmaker_1.player.domain.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.PlayerRepository
import com.practicum.playlistmaker_1.player.ui.models.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun release() {
        repository.release()
    }

    override fun getPosition(): Long = repository.getPosition()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }

}