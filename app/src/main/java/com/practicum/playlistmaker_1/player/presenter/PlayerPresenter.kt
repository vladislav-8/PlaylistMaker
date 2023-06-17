package com.practicum.playlistmaker_1.player.presenter

import com.practicum.playlistmaker_1.domain.api.PlayerInteractor
import com.practicum.playlistmaker_1.domain.models.PlayerState
import com.practicum.playlistmaker_1.player.PlayTimer
import com.practicum.playlistmaker_1.player.PlayerView

class PlayerPresenter(
    private var view: PlayerView?,
    private val interactor: PlayerInteractor,
) {

    private val playTimer: PlayTimer = PlayTimer(interactor) { view?.setDuration(it) }

    init {
        interactor.preparePlayer()
        interactor.setTrackCompletionListener {
            view?.setPlayIcon()
            playTimer.pauseTimer()
            view?.setDuration("00:00")
        }
    }

    private fun startPlayer() {
        interactor.startPlayer()
        view?.setPauseIcon()
        playTimer.startTimer()
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        view?.setPlayIcon()
        playTimer.pauseTimer()
    }

    fun onViewPaused() {
        pausePlayer()
        playTimer.pauseTimer()
    }

    fun onViewDestroyed() {
        playTimer.pauseTimer()
        view = null
    }

    fun playbackControl() {
        when (interactor.getPlayerState()) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }

            PlayerState.DEFAULT -> {
                interactor.preparePlayer()
                startPlayer()
            }
        }
    }
}
