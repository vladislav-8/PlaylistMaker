package com.practicum.playlistmaker_1.search.domain.impl

import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.domain.api.SearchRepository
import com.practicum.playlistmaker_1.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_1.search.domain.models.NetworkError

class SearchInteractorImpl(val repository: SearchRepository): SearchInteractor {

    override fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit) {
        repository.searchTracks(query, onSuccess, onError)
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory().toList()
    }

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }

}