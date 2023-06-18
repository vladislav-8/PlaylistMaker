package com.practicum.playlistmaker_1.search.data.network

import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(TracksApi::class.java)

    override fun doRequest(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (NetworkError) -> Unit,
    ) {
        api.searchTrack(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: retrofit2.Response<TrackResponse>,
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            onSuccess.invoke(response.body()?.results!!)
                        } else {
                            onError.invoke(NetworkError.EMPTY_RESULT)
                        }
                    }
                    else ->
                        onError.invoke(NetworkError.CONNECTION_ERROR)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                onError.invoke(NetworkError.CONNECTION_ERROR)
            }
        })
    }

    companion object {
        const val baseUrl = "https://itunes.apple.com"
    }

}