package com.practicum.playlistmaker_1.search.domain

import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.domain.models.NetworkError

interface SearchInteractor {
    fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}