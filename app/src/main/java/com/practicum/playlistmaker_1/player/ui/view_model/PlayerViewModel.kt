package com.practicum.playlistmaker_1.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker_1.player.domain.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.models.PlayerState
import com.practicum.playlistmaker_1.util.formatAsTime

class PlayerViewModel(val playerInteractor: PlayerInteractor) : ViewModel() {

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) handler.removeCallbacks(time)
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val time = object : Runnable {
        override fun run() {
            val position = playerInteractor.getPosition()
            timeLiveData.postValue(position.formatAsTime())
            handler.postDelayed(this, TRACK_TIME_DELAY)
        }
    }

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    fun prepare(url: String) {
        handler.removeCallbacks(time)
        playerInteractor.preparePlayer(url)
    }

    fun play() {
        playerInteractor.startPlayer()
        handler.post(time)
    }

    fun pause() {
        playerInteractor.pausePlayer()
        handler.removeCallbacks(time)
    }

    fun release() {
        playerInteractor.reset()
        handler.removeCallbacks(time)
    }

    companion object {
        private const val TRACK_TIME_DELAY = 300L
    }
}