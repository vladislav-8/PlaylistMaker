package com.practicum.playlistmaker_1.media_library.data.local_storage


interface LocalStorage {
    suspend fun saveImageToPrivateStorage(uri: String)

    fun saveCurrentPlaylistId(id: Long)

    fun getCurrentPlaylistId(): Long
}