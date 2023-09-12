package com.practicum.playlistmaker_1.search.data.impl

import com.practicum.playlistmaker_1.search.domain.api.SearchRepository
import com.practicum.playlistmaker_1.search.data.network.NetworkClient
import com.practicum.playlistmaker_1.search.data.network.TrackSearchResponse
import com.practicum.playlistmaker_1.search.data.network.TrackSearchRequest
import com.practicum.playlistmaker_1.search.data.storage.SearchHistoryStorage
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.common.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storage: SearchHistoryStorage
) : SearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl60,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getHistory(): List<Track> {
        return storage.getHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        storage.saveHistory(tracks)
    }

}