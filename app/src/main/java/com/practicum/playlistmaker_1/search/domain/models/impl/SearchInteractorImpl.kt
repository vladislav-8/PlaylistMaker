package com.practicum.playlistmaker_1.search.domain.models.impl

import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.domain.api.SearchRepository
import com.practicum.playlistmaker_1.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_1.common.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(val repository: SearchRepository): SearchInteractor {

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory().toList()
    }

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }

}