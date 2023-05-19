package com.practicum.playlistmaker_1.domain.api

interface AudioPlayerInteractor {
    fun startPlayer()
    fun startTimer()
    fun pausePlayer()
    fun preparePlayer(previewUrl: String)
    fun playbackControl()
    fun updateTimer(): Runnable
}