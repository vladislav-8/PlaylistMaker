package com.practicum.playlistmaker_1.db

import com.practicum.playlistmaker_1.search.domain.models.Track
import java.util.Calendar

class TrackDbConverter {

    fun mapFromTrackEntityToTrack(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
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
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            Calendar.getInstance().timeInMillis
        )
    }
}
