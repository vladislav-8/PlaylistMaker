package com.practicum.playlistmaker_1.domain.impl


import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker_1.presentation.player.PlayerActivity
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker_1.domain.enums.PlayerState
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerInteractorImpl : AudioPlayerInteractor {

    lateinit var playerBinding: ActivityPlayerBinding
    var mediaPlayer = MediaPlayer()
    var playerState = PlayerState.STATE_DEFAULT
    val handler = Handler(Looper.getMainLooper())

    override fun startPlayer() {
        mediaPlayer.start()
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
        playerState = PlayerState.STATE_PLAYING
    }

    override fun startTimer() {
        handler.post(
            updateTimer()
        )
    }

    override fun pausePlayer() {
        handler.removeCallbacks(updateTimer())
        mediaPlayer.pause()
        playerBinding.playButton.setImageResource(R.drawable.ic_play)
        playerState = PlayerState.STATE_PAUSED
    }

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerBinding.playButton.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            handler.removeCallbacks(updateTimer())
            playerBinding.playButton.setImageResource(R.drawable.ic_play)
            playerBinding.time.text = PlayerActivity.CURRENT_TIME_ZERO
        }
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                startTimer()
            }

            else -> {}
        }
    }

    override fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    playerBinding.time.text = SimpleDateFormat(
                        "mm:ss", Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, PlayerActivity.PLAYTIME_DELAY)
                }
            }
        }
    }
}
