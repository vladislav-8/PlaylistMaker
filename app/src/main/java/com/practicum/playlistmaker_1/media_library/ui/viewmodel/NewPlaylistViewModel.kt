package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun savePlaylist(playlist: PlaylistModel) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }
}