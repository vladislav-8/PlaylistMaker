package com.practicum.playlistmaker_1.player.domain

import com.practicum.playlistmaker_1.player.ui.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}