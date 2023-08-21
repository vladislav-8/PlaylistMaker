package com.practicum.playlistmaker_1.media_library.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksInteractor
import com.practicum.playlistmaker_1.media_library.ui.models.FavouriteTracksState
import com.practicum.playlistmaker_1.common.util.CLICK_DEBOUNCE_DELAY_MILLIS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private val stateLiveData = MutableLiveData<FavouriteTracksState>()
    fun observeState(): LiveData<FavouriteTracksState> = stateLiveData

    fun getFavouriteTracks() {
        viewModelScope.launch {
            favouriteTracksInteractor
                .getFavoritesTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        renderState(FavouriteTracksState.Empty)
                    }
                    else {
                        renderState(FavouriteTracksState.Content(tracks))
                    }
                }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun renderState(state: FavouriteTracksState) {
        stateLiveData.postValue(state)
    }
}