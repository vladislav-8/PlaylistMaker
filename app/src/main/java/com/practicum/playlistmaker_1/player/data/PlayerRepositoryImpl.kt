package com.practicum.playlistmaker_1.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker_1.player.domain.repository.PlayerRepository
import com.practicum.playlistmaker_1.player.domain.models.PlayerState

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var stateCallback: ((PlayerState) -> Unit)? = null
    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateCallback?.invoke(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            stateCallback?.invoke(PlayerState.STATE_COMPLETE)
        }
    }
    override fun startPlayer() {
        mediaPlayer.start()
        stateCallback?.invoke(PlayerState.STATE_PLAYING)
    }
    override fun pausePlayer() {
        mediaPlayer.pause()
        stateCallback?.invoke(PlayerState.STATE_PAUSED)
    }
    override fun reset() {
        mediaPlayer.reset()
    }
    override fun getPosition () = mediaPlayer.currentPosition.toLong()
    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }

}

