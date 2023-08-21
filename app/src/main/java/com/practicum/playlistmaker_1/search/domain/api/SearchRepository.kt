package com.practicum.playlistmaker_1.search.domain.api

import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}