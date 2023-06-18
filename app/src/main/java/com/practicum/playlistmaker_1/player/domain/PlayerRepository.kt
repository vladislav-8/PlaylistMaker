package com.practicum.playlistmaker_1.player.domain

import com.practicum.playlistmaker_1.player.ui.models.PlayerState

interface PlayerRepository {
    var playerState: PlayerState
    var completionListener: () -> Unit
    fun prepare()
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
}