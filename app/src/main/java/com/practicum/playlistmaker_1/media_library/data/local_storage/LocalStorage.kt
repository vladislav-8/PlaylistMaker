package com.practicum.playlistmaker_1.media_library.data.local_storage

import android.net.Uri

interface LocalStorage {
    suspend fun saveImageToPrivateStorage(uri: Uri)

    fun saveCurrentPlaylistId(id: Long)

    fun getCurrentPlaylistId(): Long
}