package com.practicum.playlistmaker_1.media_library.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    fun saveToLocalStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.saveImageToPrivateStorage(uri)
        }
    }

}