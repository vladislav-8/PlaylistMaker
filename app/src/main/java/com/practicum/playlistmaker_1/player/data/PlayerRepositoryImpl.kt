package com.practicum.playlistmaker_1.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker_1.player.domain.PlayerRepository
import com.practicum.playlistmaker_1.player.ui.models.PlayerState

class   PlayerRepositoryImpl (private val trackUrl: String): PlayerRepository {

    override var playerState = PlayerState.DEFAULT
    private val mediaPlayer = MediaPlayer()
    override var completionListener: () -> Unit = { }

    override fun prepare() {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            completionListener.invoke()
        }
    }

    override fun start() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}

