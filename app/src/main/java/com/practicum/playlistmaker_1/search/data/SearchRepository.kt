package com.practicum.playlistmaker_1.search.data

import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track

interface SearchRepository {
    fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}