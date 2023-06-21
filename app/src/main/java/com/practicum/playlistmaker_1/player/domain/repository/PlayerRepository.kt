package com.practicum.playlistmaker_1.player.domain.repository

import com.practicum.playlistmaker_1.player.domain.models.PlayerState

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun reset()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}