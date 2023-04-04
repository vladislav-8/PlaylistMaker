package com.practicum.playlistmaker_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.practicum.playlistmaker_1.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    lateinit var searchBinding: ActivitySearchBinding
    private val tracksBaseUrl = "https://itunes.apple.com"
    private var searchInputQuery = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(tracksBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tracksApi = retrofit.create(TracksApi::class.java)

    private val trackAdapter = TrackAdapter {
        searchHistory.addTrack(it)
    }
    private val historyAdapter = TrackAdapter {
        searchHistory.addTrack(it)
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
        const val TRACKS_HISTORY_KEY = "tracks_history_key"
        const val TRACKS_HISTORY_SIZE = 10
    }

    private val simpleTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, s1: Int, s2: Int, s3: Int) {
            searchInputQuery = s.toString()
            if (s.isNullOrEmpty()) {
                searchBinding.clearImageView.visibility = GONE
            } else searchBinding.clearImageView.visibility = VISIBLE

            if (searchBinding.inputEditText.hasFocus() && searchInputQuery.isNotEmpty()) {
                showPlaceholder(Placeholder.SEARCH_RESULT)
                searchBinding.searchHistoryRecycler.visibility = GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    private val searchHistory by lazy {
        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        SearchHistory(sharedPrefs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        searchBinding.inputEditText.requestFocus()

        searchBinding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        searchBinding.clearImageView.setOnClickListener { clearSearchForm() }
        searchBinding.inputEditText.addTextChangedListener(simpleTextWatcher)

        searchBinding.searchRecycler.adapter = trackAdapter
        searchBinding.searchHistoryRecycler.adapter = historyAdapter

        searchBinding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchBinding.inputEditText.text.isEmpty()) {
                showPlaceholder(Placeholder.SEARCH_RESULT)
            }
        }

        searchBinding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }
        refresh()

        if (searchBinding.inputEditText.text.isEmpty()) {
            historyAdapter.tracks = searchHistory.getTracks()
            if (historyAdapter.tracks.isNotEmpty()) {
                showPlaceholder(Placeholder.TRACKS_HISTORY)
            }
        }

        searchBinding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            showPlaceholder(Placeholder.SEARCH_RESULT)
        }
    }

    private fun refresh() {
        searchBinding.refreshButton.setOnClickListener {
            searchTrack()
        }
    }

    private fun searchTrack() {
        tracksApi.searchTrack(searchBinding.inputEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackAdapter.tracks = response.body()?.results!! as MutableList<Track>
                                showPlaceholder(Placeholder.SEARCH_RESULT)
                            } else {
                                showPlaceholder(Placeholder.NOTHING_FOUND)
                            }
                        }
                        else -> {
                            showPlaceholder(Placeholder.INTERNET_PROBLEM)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showPlaceholder(Placeholder.INTERNET_PROBLEM)
                }
            })
    }

    fun showPlaceholder(placeholder: Placeholder) {
        when (placeholder) {
            Placeholder.NOTHING_FOUND -> {
                searchBinding.searchRecycler.visibility = GONE
                searchBinding.internetProblem.visibility = GONE
                searchBinding.searchHistoryLayout.visibility = GONE
                searchBinding.nothingFound.visibility = VISIBLE

            }
            Placeholder.INTERNET_PROBLEM -> {
                searchBinding.searchRecycler.visibility = GONE
                searchBinding.nothingFound.visibility = GONE
                searchBinding.searchHistoryLayout.visibility = GONE
                searchBinding.internetProblem.visibility = VISIBLE
            }

            Placeholder.SEARCH_RESULT -> {
                searchBinding.nothingFound.visibility = GONE
                searchBinding.internetProblem.visibility = GONE
                searchBinding.searchHistoryLayout.visibility = GONE
                searchBinding.searchRecycler.visibility = VISIBLE
            }
            else -> {
                searchBinding.searchRecycler.visibility = GONE
                searchBinding.nothingFound.visibility = GONE
                searchBinding.internetProblem.visibility = GONE
                searchBinding.searchHistoryLayout.visibility = VISIBLE
            }
        }
    }

    private fun clearSearchForm() {
        searchBinding.inputEditText.setText("")
        trackAdapter.tracks.clear()
        if (historyAdapter.tracks.isNotEmpty()) {
            showPlaceholder(Placeholder.TRACKS_HISTORY)
        } else {
            showPlaceholder(Placeholder.SEARCH_RESULT)
        }
        clearPlaceholders()
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clearPlaceholders() {
        searchBinding.nothingFound.visibility = GONE
        searchBinding.internetProblem.visibility = GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        if (searchInputQuery.isNotEmpty()) {
            searchBinding.inputEditText.setText(searchInputQuery)
            searchTrack()
        }
    }
}