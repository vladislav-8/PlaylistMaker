package com.practicum.playlistmaker_1.di

import com.practicum.playlistmaker_1.media_library.ui.viewmodel.FavouriteTracksViewModel
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.PlaylistsViewModel
import com.practicum.playlistmaker_1.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module {

    viewModel {
        FavouriteTracksViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}