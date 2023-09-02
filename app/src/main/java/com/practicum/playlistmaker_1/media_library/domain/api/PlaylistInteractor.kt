package com.practicum.playlistmaker_1.media_library.domain.api

import android.net.Uri
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Int)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylists(playlist: Playlist)

    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>

    suspend fun saveImageToPrivateStorage(uri: Uri)
}