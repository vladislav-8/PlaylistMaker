package com.practicum.playlistmaker_1.search.data.impl

import com.practicum.playlistmaker_1.search.domain.api.SearchRepository
import com.practicum.playlistmaker_1.search.data.network.NetworkClient
import com.practicum.playlistmaker_1.search.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track

class SearchRepositoryImpl (private val networkClient: NetworkClient, private val storage: SearchHistoryStorage):
    SearchRepository {

    override fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit) {
        networkClient.doRequest(query, onSuccess, onError)
    }

    override fun getHistory(): List<Track> {
        return storage.getHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        storage.saveHistory(tracks)
    }

}