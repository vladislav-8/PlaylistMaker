package com.practicum.playlistmaker_1.di

import android.media.MediaPlayer
import com.practicum.playlistmaker_1.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker_1.player.domain.PlayerInteractor
import com.practicum.playlistmaker_1.player.domain.PlayerRepository
import com.practicum.playlistmaker_1.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker_1.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    viewModel {
        PlayerViewModel(
            get()
        )
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

}