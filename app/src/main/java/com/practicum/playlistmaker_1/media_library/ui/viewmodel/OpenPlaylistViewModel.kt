package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.sharing.SharingInteractor
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    init {
        viewModelScope.launch {
            getCurrentPlaylistById(playlistInteractor.getCurrentPlaylistId())
        }
    }

    private fun getCurrentPlaylistById(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect {
                _playlist.postValue(it)
            }
        }
    }

    fun getPlaylist(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }

    fun getTracks(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(id).collect {
                _tracks.postValue(it)
            }
        }
    }
}