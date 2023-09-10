package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.sharing.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

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
            playlistInteractor.deleteTrackFromPlaylist(track, playlist).collect {
                _isAlreadyInPlaylist.postValue(Pair(playlist.title, it))
            }
        }
    }

    fun isEmptyTracks(): Boolean {
        return tracks.value?.isEmpty() ?: true
    }

    fun sharePlaylist(resources: Resources): String {
        var sharePlaylist = playlist.value?.title + "\n" +
                playlist.value?.description + "\n" +
                resources.getQuantityString(
                    R.plurals.plural_tracks,
                    tracks.value?.size ?: 0,
                    tracks.value?.size ?: 0
                ) + "\n"
        tracks.value?.forEachIndexed { index, track ->
            sharePlaylist += "${index + 1}. ${track.artistName} - ${track.trackName} (" + resources.getQuantityString(
                R.plurals.plural_minutes,
                SimpleDateFormat("mm", Locale.getDefault()).format(track.trackTimeMillis)
                    .toInt(),
                SimpleDateFormat("mm", Locale.getDefault()).format(track.trackTimeMillis)
                    .toInt()
            ) + ") \n"
        }
        return sharePlaylist
    }

    fun shareTracks(tracks: String) {
        sharingInteractor.sharePlaylist(tracks)
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(id)
        }
    }
}