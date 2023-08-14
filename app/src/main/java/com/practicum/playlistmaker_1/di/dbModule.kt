package com.practicum.playlistmaker_1.di

import androidx.room.Room
import com.practicum.playlistmaker_1.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}