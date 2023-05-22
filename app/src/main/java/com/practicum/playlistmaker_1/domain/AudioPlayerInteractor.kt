package com.practicum.playlistmaker_1.domain

interface AudioPlayerInteractor {
    fun startPlayer()
    fun startTimer()
    fun pausePlayer()
    fun preparePlayer(previewUrl: String)
    fun playbackControl()
    fun updateTimer(): Runnable
}