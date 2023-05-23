package com.practicum.playlistmaker_1.data.repository


import android.media.MediaPlayer
import com.practicum.playlistmaker_1.domain.repository.PlayerInteractorRepository
import com.practicum.playlistmaker_1.domain.models.PlayerState

class PlayerInteractorRepositoryImpl : PlayerInteractorRepository {

    var mediaPlayer = MediaPlayer()
    private var stateCallback: ((PlayerState) -> Unit)? = null

    override fun startPlayer() {
        mediaPlayer.start()
        stateCallback?.invoke(PlayerState.STATE_PLAYING)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        stateCallback?.invoke(PlayerState.STATE_PAUSED)
    }

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

    override fun release() {
        mediaPlayer.release()
    }

    override fun getPosition() = mediaPlayer.currentPosition.toLong()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }
}
