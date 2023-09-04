package com.practicum.playlistmaker_1.di

import com.practicum.playlistmaker_1.search.domain.api.SearchRepository
import com.practicum.playlistmaker_1.search.data.impl.SearchRepositoryImpl
import com.practicum.playlistmaker_1.search.data.network.NetworkClient
import com.practicum.playlistmaker_1.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_1.search.data.network.TracksApi
import com.practicum.playlistmaker_1.search.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker_1.search.data.storage.SharedPrefsHistoryStorage
import com.practicum.playlistmaker_1.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_1.search.domain.models.impl.SearchInteractorImpl
import com.practicum.playlistmaker_1.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker_1.common.util.BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    viewModel {
        SearchViewModel(
            get()
        )
    }

    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), storage = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(api = get(), context = get())
    }

    single<SearchHistoryStorage> {
        SharedPrefsHistoryStorage(sharedPrefs = get())
    }

    single<TracksApi> {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TracksApi::class.java)
    }
}