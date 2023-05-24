package com.practicum.playlistmaker_1.domain.api

import com.practicum.playlistmaker_1.domain.models.PlayerState

interface TrackPlayer {
    var playerState: PlayerState
    var completionListener: () -> Unit
    fun prepare()
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
}