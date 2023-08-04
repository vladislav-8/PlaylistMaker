package com.practicum.playlistmaker_1.search.data.network


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}