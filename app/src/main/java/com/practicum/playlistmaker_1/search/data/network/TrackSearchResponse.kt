package com.practicum.playlistmaker_1.search.data.network

import com.practicum.playlistmaker_1.search.domain.models.Track

class TrackSearchResponse(
    val results: List<Track>
) : Response()
