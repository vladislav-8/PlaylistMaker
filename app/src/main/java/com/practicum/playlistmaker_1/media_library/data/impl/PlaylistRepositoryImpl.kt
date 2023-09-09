package com.practicum.playlistmaker_1.media_library.data.impl

import android.net.Uri
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_1.db.AppDatabase
import com.practicum.playlistmaker_1.db.converters.DbConverter
import com.practicum.playlistmaker_1.db.entity.PlaylistEntity
import com.practicum.playlistmaker_1.media_library.data.local_storage.LocalStorage
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class   PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConverter,
    private val localStorage: LocalStorage,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.PlaylistDao()
            .addPlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(id: Int) {
        appDatabase.PlaylistDao().deletePlaylist(id)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.PlaylistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylists(playlist: Playlist) {
        appDatabase.PlaylistDao()
            .updatePlaylist(dbConvertor.mapFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>  =
        flow {
            val gson = GsonBuilder().create()
            val arrayTrackType = object : TypeToken<ArrayList<Track>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Track>()

            var isInPlaylist = false

            playlistTracks.forEach {
                if (it.trackId == track.trackId) {
                    isInPlaylist = true
                }
            }

            if (!isInPlaylist) {
                playlistTracks.add(track)
                playlist.trackList = gson.toJson(playlistTracks)

                playlist.size++
                updatePlaylists(playlist)

                emit(true)
            } else {
                emit(false)
            }
        }

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        localStorage.saveImageToPrivateStorage(uri)
    }

    override suspend fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        emit(dbConvertor.mapFromPlaylistEntityToPlaylist(appDatabase.PlaylistDao().getPlaylistById(id)))
    }

    override suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>> = flow {
        val gson = GsonBuilder().create()
        val listTrackType = object : TypeToken<List<Track>>() {}.type

        val tracksString = appDatabase.PlaylistDao().getTracksFromPlaylist(id)
        val tracks = gson.fromJson(tracksString, listTrackType) ?: listOf<Track>()

        emit(tracks.reversed())
    }

    override suspend fun saveCurrentPlaylistId(id: Long) {
        localStorage.saveCurrentPlaylistId(id)
    }

    override suspend fun getCurrentPlaylistId(): Long {
        return localStorage.getCurrentPlaylistId()
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlists ->
            dbConvertor.mapFromPlaylistEntityToPlaylist(
                playlists
            )
        }
    }
}