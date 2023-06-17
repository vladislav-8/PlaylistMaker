package com.practicum.playlistmaker_1.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker_1.player.ui.models.NavigationState

class MainActivityViewModel: ViewModel() {
    private val _navigationLiveData = MutableLiveData<NavigationState>()
    val navigationLiveData: LiveData<NavigationState> = _navigationLiveData

    fun openSearchScreen() {
        _navigationLiveData.value = NavigationState.SearchScreen
    }

    fun openMediaLibraryScreen() {
        _navigationLiveData.value = NavigationState.MediaLibraryScreen
    }

    fun openSettingsScreen() {
        _navigationLiveData.value = NavigationState.SettingsScreen
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainActivityViewModel()
                }
            }
    }
}