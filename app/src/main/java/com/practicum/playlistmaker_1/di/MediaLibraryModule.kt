package com.practicum.playlistmaker_1.di

import com.practicum.playlistmaker_1.db.converters.DbConverter
import com.practicum.playlistmaker_1.media_library.data.impl.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker_1.media_library.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker_1.media_library.data.local_storage.LocalStorage
import com.practicum.playlistmaker_1.media_library.data.impl.LocalStorageImpl
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksInteractor
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksRepository
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker_1.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker_1.media_library.domain.impl.FavouriteTracksInteractorImpl
import com.practicum.playlistmaker_1.media_library.domain.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.FavouriteTracksViewModel
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.NewPlaylistViewModel
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module {

    viewModel {
        FavouriteTracksViewModel(favouriteTracksInteractor = get())
    }

    viewModel {
        NewPlaylistViewModel(playlistInteractor = get())
    }

    viewModel {
        PlaylistViewModel(playlistInteractor = get())
    }

    factory {
        DbConverter()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(favouriteTracksRepository = get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(appDatabase = get(), dbConvertor = get(), localStorage = get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(playlistRepository = get())
    }

    single<LocalStorage> {
        LocalStorageImpl(context = get())
    }
}