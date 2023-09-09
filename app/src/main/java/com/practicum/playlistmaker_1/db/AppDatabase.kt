package com.practicum.playlistmaker_1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker_1.db.dao.PlaylistDao
import com.practicum.playlistmaker_1.db.dao.TrackDao
import com.practicum.playlistmaker_1.db.entity.PlaylistEntity
import com.practicum.playlistmaker_1.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class, PlaylistEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun TrackDao(): TrackDao
    abstract fun PlaylistDao(): PlaylistDao
}