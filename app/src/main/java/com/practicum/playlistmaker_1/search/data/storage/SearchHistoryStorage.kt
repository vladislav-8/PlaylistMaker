package com.practicum.playlistmaker_1.search.data.storage

import com.practicum.playlistmaker_1.search.domain.models.Track

interface SearchHistoryStorage {
    fun saveHistory(tracks: List<Track>)
    fun getHistory(): List<Track>
    fun clearHistory()
}