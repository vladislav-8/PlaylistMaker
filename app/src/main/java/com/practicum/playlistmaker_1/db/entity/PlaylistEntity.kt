package com.practicum.playlistmaker_1.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker_1.db.entity.PlaylistEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
class PlaylistEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val imageUri: String,
    val trackList: String,
    val size: Int,
) {

    companion object {
        const val TABLE_NAME = "playlist_table"
    }
}