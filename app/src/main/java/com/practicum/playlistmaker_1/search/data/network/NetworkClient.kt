package com.practicum.playlistmaker_1.search.data.network

import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track

interface NetworkClient {
    fun doRequest(query: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
}