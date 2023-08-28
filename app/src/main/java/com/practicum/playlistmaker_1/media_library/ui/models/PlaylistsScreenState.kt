package com.practicum.playlistmaker_1.media_library.ui.models

import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel

sealed class PlaylistsScreenState(playlists: List<PlaylistModel>?) {
    object Empty : PlaylistsScreenState(null)
    class Filled(val playlists: List<PlaylistModel>) : PlaylistsScreenState(playlists)
}
