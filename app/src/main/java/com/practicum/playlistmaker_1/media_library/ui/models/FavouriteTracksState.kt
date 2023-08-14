package com.practicum.playlistmaker_1.media_library.ui.models

import com.practicum.playlistmaker_1.search.domain.models.Track

sealed interface FavouriteTracksState {

    data class Content(
        val tracks: List<Track>
    ) : FavouriteTracksState

    object Empty : FavouriteTracksState
}