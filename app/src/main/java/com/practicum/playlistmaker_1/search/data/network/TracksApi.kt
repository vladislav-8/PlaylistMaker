package com.practicum.playlistmaker_1.search.data.network


import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TrackSearchResponse
}