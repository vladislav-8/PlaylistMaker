package com.practicum.playlistmaker_1.media_library.domain.impl

import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun addPlaylist(playlist: PlaylistModel) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Int) {
        playlistRepository.deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylists(playlist: PlaylistModel) {
        playlistRepository.updatePlaylists(playlist)
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: PlaylistModel): Flow<Boolean> {
        return playlistRepository.addTrackToPlayList(track, playlist)
    }
}