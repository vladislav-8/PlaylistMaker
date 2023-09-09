package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.sharing.SharingInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isAlreadyInPlaylist = MutableLiveData<Pair<String, Boolean>>()
    val isAlreadyInPlaylist: LiveData<Pair<String, Boolean>> = _isAlreadyInPlaylist

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

    fun getTracks(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(id).collect {
                _tracks.postValue(it)
            }
        }
    }

    fun deleteTracks(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track,playlist).collect {
                _isAlreadyInPlaylist.postValue(Pair(playlist.title, it))
            }
        }
    }
}