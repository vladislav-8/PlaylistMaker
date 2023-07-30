package com.practicum.playlistmaker_1.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.models.PlayerState
import com.practicum.playlistmaker_1.util.DELAY_TIME
import com.practicum.playlistmaker_1.util.formatAsTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(val playerInteractor: PlayerInteractor) : ViewModel() {

    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) timerJob?.cancel()
        }
    }

    private fun startTimer () {
        timerJob = viewModelScope.launch {
            while(isActive) {
                delay(DELAY_TIME)
                timeLiveData.postValue(playerInteractor.getPosition().formatAsTime())
            }
        }
    }

    fun prepare(url: String) {
        timerJob?.cancel()
        playerInteractor.preparePlayer(url)
    }

    fun play() {
        playerInteractor.startPlayer()
        startTimer()
    }

    fun pause() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun reset() {
        playerInteractor.reset()
        timerJob?.cancel()
    }
}