package com.practicum.playlistmaker_1.player

import com.practicum.playlistmaker_1.domain.models.Track

interface PlayerView {
    fun setPlayIcon()
    fun setPauseIcon()
    fun drawPlayer(track: Track)
    fun setDuration(ms: String)
}