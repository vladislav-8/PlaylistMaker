package com.practicum.playlistmaker_1.media_library.domain.models

import android.net.Uri

data class Playlist(
    val id: Long = 0,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    var trackList: String,
    var size: Int,
)
