package com.practicum.playlistmaker_1.media_library.domain.impl

import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksInteractor
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksRepository
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
) : FavouriteTracksInteractor {

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return favouriteTracksRepository.getFavoritesTracks()
    }

    override suspend fun isFavoriteTrack(trackId: Long): Flow<Boolean> {
        return favouriteTracksRepository.isFavoriteTrack(trackId)
    }

    override suspend fun addToFavorites(track: Track) {
        favouriteTracksRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(trackId: Long) {
        favouriteTracksRepository.deleteFromFavorites(trackId)
    }
}