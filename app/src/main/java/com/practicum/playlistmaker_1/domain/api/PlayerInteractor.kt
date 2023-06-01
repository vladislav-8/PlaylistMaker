package com.practicum.playlistmaker_1.domain.api

import com.practicum.playlistmaker_1.domain.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun getCurrentPosition(): Int
    fun getPlayerState(): PlayerState
    fun setTrackCompletionListener(listener: (() -> Unit))
}