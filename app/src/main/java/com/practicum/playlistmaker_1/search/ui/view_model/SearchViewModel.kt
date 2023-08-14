package com.practicum.playlistmaker_1.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker_1.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.ui.models.SearchState
import com.practicum.playlistmaker_1.common.util.CLICK_DEBOUNCE_DELAY_MILLIS
import com.practicum.playlistmaker_1.common.util.maxHistorySize
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {

    private var isClickAllowed = true
    private var searchJob: Job? = null

    private val historyList = ArrayList<Track>()

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private var latestSearchText: String? = null

    init {
        historyList.addAll(searchInteractor.getHistory())
        _stateLiveData.postValue(SearchState.SearchHistory(historyList))
    }

    fun searchTracks(query: String) {
        if (query.isEmpty()) return

        renderState(SearchState.Loading)

        viewModelScope.launch {
            searchInteractor
                .searchTracks(query)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(searchTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (searchTracks != null) {
            tracks.addAll(searchTracks)
        }

        when {
            errorMessage != null -> {
                renderState(
                   SearchState.SearchError(error = NetworkError.CONNECTION_ERROR)
                )
            }

            tracks.isEmpty() -> {
                renderState(SearchState.SearchError(error = NetworkError.EMPTY_RESULT))
            }

            else -> {
                renderState(SearchState.SearchedTracks(tracks))
            }
        }
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

    fun clearHistory() {
        historyList.clear()
        _stateLiveData.postValue(SearchState.SearchHistory(historyList))
    }

    fun clearSearchText() {
        _stateLiveData.postValue(SearchState.SearchHistory(historyList))
    }

    fun addTrackToHistory(track: Track) {
        if (historyList.contains(track)) {
            historyList.removeAt(historyList.indexOf(track))
        } else if (historyList.size == maxHistorySize) {
            historyList.removeAt(0)
        }
        historyList.add(track)
        searchInteractor.saveHistory(historyList)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY_MILLIS)
            searchTracks(changedText)
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
}