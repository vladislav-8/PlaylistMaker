package com.practicum.playlistmaker_1.di

import com.practicum.playlistmaker_1.db.TrackDbConverter
import com.practicum.playlistmaker_1.media_library.data.FavouriteTracksRepositoryImpl
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksInteractor
import com.practicum.playlistmaker_1.media_library.domain.api.FavouriteTracksRepository
import com.practicum.playlistmaker_1.media_library.domain.impl.FavouriteTracksInteractorImpl
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.FavouriteTracksViewModel
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module {

    viewModel {
        FavouriteTracksViewModel(favouriteTracksInteractor = get())
    }

    viewModel {
        PlaylistsViewModel()
    }

    factory {
        TrackDbConverter()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(favouriteTracksRepository = get())
    }
}