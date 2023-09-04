package com.practicum.playlistmaker_1.media_library.ui.models

import com.practicum.playlistmaker_1.media_library.domain.models.Playlist

sealed class PlaylistsScreenState(playlists: List<Playlist>?) {
    object Empty : PlaylistsScreenState(null)
    class Filled(val playlists: List<Playlist>) : PlaylistsScreenState(playlists)
}
