package com.practicum.playlistmaker_1.search.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker_1.databinding.ActivitySearchBinding
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.ui.models.SearchState
import com.practicum.playlistmaker_1.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker_1.search.ui.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private var searchInputQuery = ""

    private val trackAdapter = TrackAdapter { showPlayer(it) }
    private val historyAdapter = TrackAdapter { showPlayer(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        initListeners()

        searchBinding.inputEditText.requestFocus()
        searchBinding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        searchBinding.searchRecycler.adapter = trackAdapter
        searchBinding.searchHistoryRecycler.adapter = historyAdapter

        searchBinding.inputEditText.doOnTextChanged { text, _, _, _ ->
            searchBinding.clearImageView.visibility = clearButtonVisibility(text)
            text?.let { viewModel.searchDebounce(it.toString()) }
        }

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(this)
        )[SearchViewModel::class.java]
        viewModel.stateLiveData.observe(this) { state ->
            when (state) {
                is SearchState.SearchHistory -> {
                    showHistoryList(state.tracks)
                }

                is SearchState.Loading -> {
                    showLoading()
                }

                is SearchState.SearchedTracks -> {
                    showSearchResult(state.tracks)
                }

                is SearchState.SearchError -> {
                    showErrorMessage(state.error)
                }
            }
        }


    }

    private fun initListeners() {
        searchBinding.refreshButton.setOnClickListener {
            viewModel.searchTracks(searchInputQuery)
        }
        searchBinding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
        searchBinding.clearImageView.setOnClickListener {
            viewModel.clearSearchText()
            searchBinding.inputEditText.text?.clear()
            clearContent()

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java).putExtra(
                PlayerActivity.TRACK,
                Gson().toJson(track)
            )
            startActivity(intent)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    private fun showHistoryList(tracks: List<Track>) {
        clearContent()
        historyAdapter.tracks = tracks as ArrayList<Track>
        if (tracks.isNotEmpty()) {
            searchBinding.searchHistoryLayout.visibility = VISIBLE
        }
    }

    private fun showLoading() {
        clearContent()
        searchBinding.progressBar.visibility = VISIBLE
    }

    private fun showSearchResult(tracks: List<Track>) {
        clearContent()
        trackAdapter.clearTracks()
        trackAdapter.tracks.addAll(tracks)
        searchBinding.searchRecycler.visibility = VISIBLE
    }

    private fun showErrorMessage(networkError: NetworkError) {
        clearContent()
        searchBinding.internetProblem.visibility = VISIBLE
    }

    private fun clearContent() {
        searchBinding.nothingFound.visibility = GONE
        searchBinding.internetProblem.visibility = GONE
        searchBinding.searchHistoryLayout.visibility = GONE
        searchBinding.searchRecycler.visibility = GONE
        searchBinding.progressBar.visibility = GONE
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
            viewModel.searchTracks(searchInputQuery)
        }
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
    }
}