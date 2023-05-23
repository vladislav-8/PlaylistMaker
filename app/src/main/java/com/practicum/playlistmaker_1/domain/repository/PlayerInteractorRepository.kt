package com.practicum.playlistmaker_1.domain.repository

import com.practicum.playlistmaker_1.domain.models.PlayerState

interface PlayerInteractorRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}