package com.practicum.playlistmaker_1.media_library.domain.api

import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: PlaylistModel)

    suspend fun deletePlaylist(id: Int)

    suspend fun getPlaylists(): Flow<List<PlaylistModel>>

    suspend fun updatePlaylists(playlist: PlaylistModel)

    suspend fun addTrackToPlayList(track: Track, playlist: PlaylistModel): Flow<Boolean>
}