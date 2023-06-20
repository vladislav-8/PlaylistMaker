package com.practicum.playlistmaker_1.di

import com.practicum.playlistmaker_1.sharing.SharingInteractor
import com.practicum.playlistmaker_1.sharing.data.ExternalNavigator
import com.practicum.playlistmaker_1.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker_1.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }
}