package com.practicum.playlistmaker_1.search.ui.models

import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track

sealed class SearchState {
    class SearchHistory(val tracks: List<Track>) : SearchState()
    class SearchedTracks(val tracks: List<Track>) : SearchState()
    class SearchError(val error: NetworkError) : SearchState()
    object Loading : SearchState()
}
