package com.practicum.playlistmaker_1.player.domain

import com.practicum.playlistmaker_1.player.ui.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun getCurrentPosition(): Int
    fun getPlayerState(): PlayerState
    fun setTrackCompletionListener(listener: (() -> Unit))
}