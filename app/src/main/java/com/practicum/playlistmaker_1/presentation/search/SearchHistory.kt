package com.practicum.playlistmaker_1.presentation.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker_1.presentation.search.SearchActivity.Companion.TRACKS_HISTORY_KEY
import com.practicum.playlistmaker_1.presentation.search.SearchActivity.Companion.TRACKS_HISTORY_SIZE
import com.practicum.playlistmaker_1.domain.models.Track


class SearchHistory(private val sharedPrefs: SharedPreferences) {

    fun addTrack(track: Track) {
        val tracksHistory = getTracks()
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > TRACKS_HISTORY_SIZE) tracksHistory.removeLast()
        saveTracks(tracksHistory)
    }

    fun getTracks(): MutableList<Track> {
        val json = sharedPrefs.getString(TRACKS_HISTORY_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun clear() {
        sharedPrefs.edit { remove(TRACKS_HISTORY_KEY) }
    }

    private fun saveTracks(tracks: MutableList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit().putString(TRACKS_HISTORY_KEY, json).apply()
    }
}

