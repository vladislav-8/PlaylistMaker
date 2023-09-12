package com.practicum.playlistmaker_1.media_library.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Playlist(
    var id: Long = 0,
    var title: String,
    val description: String,
    @SerializedName("imageUri")
    val imageUri: String? = null,
    var trackList: String,
    var size: Int,
): Serializable