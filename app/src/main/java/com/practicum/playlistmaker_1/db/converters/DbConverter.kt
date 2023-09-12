package com.practicum.playlistmaker_1.db.converters

import androidx.core.net.toUri
import com.practicum.playlistmaker_1.db.entity.PlaylistEntity
import com.practicum.playlistmaker_1.db.entity.TrackEntity
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.search.domain.models.Track
import java.util.Calendar

class DbConverter {
    fun mapFromTrackEntityToTrack(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl60 = from.artworkUrl60,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
        )
    }

    fun mapFromTrackToTrackEntity(from: Track): TrackEntity {
        return TrackEntity(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl60 = from.artworkUrl60,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            Calendar.getInstance().timeInMillis
        )
    }

    fun mapFromPlaylistEntityToPlaylist(from: PlaylistEntity): Playlist {
        return Playlist(
            id = from.id,
            title = from.title,
            description = from.description,
            imageUri = from.imageUri,
            trackList = from.trackList,
            size = from.size
        )
    }

    fun mapFromPlaylistToPlaylistEntity(from: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = from.id,
            title = from.title,
            description = from.description,
            imageUri = from.imageUri.toString(),
            trackList = from.trackList,
            size = from.size
        )
    }
}