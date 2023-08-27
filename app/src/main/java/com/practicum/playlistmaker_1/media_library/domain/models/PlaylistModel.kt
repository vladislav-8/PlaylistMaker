package com.practicum.playlistmaker_1.media_library.domain.models

import android.net.Uri

data class PlaylistModel(
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    var trackList: String,
    var size: Int,
)
