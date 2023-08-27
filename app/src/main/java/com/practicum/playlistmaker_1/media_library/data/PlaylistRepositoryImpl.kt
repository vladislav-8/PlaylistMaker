package com.practicum.playlistmaker_1.media_library.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_1.db.AppDatabase
import com.practicum.playlistmaker_1.db.DbConverter
import com.practicum.playlistmaker_1.db.entity.PlaylistEntity
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConverter,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: PlaylistModel) {
        appDatabase.PlaylistDao()
            .addPlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(id: Int) {
        appDatabase.PlaylistDao().deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> = flow {
        val playlists = appDatabase.PlaylistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylists(playlist: PlaylistModel) {
        appDatabase.PlaylistDao()
            .updatePlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: PlaylistModel): Flow<Boolean> =
        flow {
            val gson = GsonBuilder().create()
            val arrayTrackType = object : TypeToken<ArrayList<Int>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Int>()

            if (!playlistTracks.contains(track.trackId)) {
                playlistTracks.add(track.trackId)
                playlist.trackList = gson.toJson(playlistTracks)
                playlist.size++
                updatePlaylists(playlist)
                emit(true)
            } else {
                emit(false)
            }
        }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistModel> {
        return playlists.map { playlists ->
            dbConvertor.mapFromPlaylistEntityToPlaylist(
                playlists
            )
        }
    }
}