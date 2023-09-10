package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.media_library.ui.models.PlaylistsScreenState
import kotlinx.coroutines.launch


class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsScreenState>()
    val stateLiveData: LiveData<PlaylistsScreenState> = _stateLiveData

    init {
        _stateLiveData.postValue(PlaylistsScreenState.Empty)
    }

    fun fillData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _stateLiveData.postValue(PlaylistsScreenState.Empty)
        } else {
            _stateLiveData.postValue(PlaylistsScreenState.Filled(playlists))
        }
    }

   fun saveCurrentPlaylistId(id: Long) {
        viewModelScope.launch {
            playlistInteractor.saveCurrentPlaylistId(id)
        }
    }
}