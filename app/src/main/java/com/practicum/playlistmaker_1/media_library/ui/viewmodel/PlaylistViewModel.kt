package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.ui.models.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _state = MutableLiveData<PlaylistsScreenState>()
    val state: LiveData<PlaylistsScreenState> = _state

    init {
        _state.postValue(PlaylistsScreenState.Empty)
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) {
                    _state.postValue(PlaylistsScreenState.Empty)
                } else {
                    _state.postValue(PlaylistsScreenState.Filled(it))
                }
            }
        }
    }
}