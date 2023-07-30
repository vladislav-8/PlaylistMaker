package com.practicum.playlistmaker_1.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.practicum.playlistmaker_1.databinding.FragmentSearchBinding
import com.practicum.playlistmaker_1.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker_1.search.domain.models.NetworkError
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.search.ui.TrackAdapter
import com.practicum.playlistmaker_1.search.ui.models.SearchState
import com.practicum.playlistmaker_1.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker_1.util.EXTRA_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var searchBinding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    private var searchInputQuery = ""

    private val trackAdapter = TrackAdapter { showPlayer(it) }
    private val historyAdapter = TrackAdapter { showPlayer(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initAdapters()

        searchBinding.inputEditText.requestFocus()

        searchBinding.inputEditText.doOnTextChanged { text, _, _, _ ->
            searchBinding.clearImageView.visibility = clearButtonVisibility(text)
            text?.let { viewModel.searchDebounce(it.toString()) }
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SearchState) {
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

    private fun initAdapters() {
        searchBinding.searchRecycler.adapter = trackAdapter
        searchBinding.searchHistoryRecycler.adapter = historyAdapter
    }

    private fun initListeners() {
        searchBinding.refreshButton.setOnClickListener {
            viewModel.searchTracks(searchInputQuery)
        }
        searchBinding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
        searchBinding.clearImageView.setOnClickListener {
            searchClearShowHistory()
        }
    }


    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackToHistory(track)
            val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(EXTRA_KEY, track)
            }
            startActivity(intent)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchClearShowHistory() {
        viewModel.clearSearchText()
        searchBinding.inputEditText.text?.clear()
        clearContent()

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showHistoryList(tracks: List<Track>) {
        clearContent()
        historyAdapter.tracks = tracks as ArrayList<Track>
        if (tracks.isNotEmpty()) {
            searchBinding.searchHistoryLayout.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        clearContent()
        searchBinding.progressBar.visibility = View.VISIBLE
    }

    private fun showSearchResult(tracks: List<Track>) {
        clearContent()
        trackAdapter.clearTracks()
        trackAdapter.tracks.addAll(tracks)
        searchBinding.searchRecycler.visibility = View.VISIBLE
    }

    private fun showErrorMessage(error: NetworkError) {
        clearContent()
        when (error) {
            NetworkError.EMPTY_RESULT -> {
                searchBinding.searchRecycler.visibility = View.GONE
                searchBinding.internetProblem.visibility = View.GONE
                searchBinding.searchHistoryLayout.visibility = View.GONE
                searchBinding.nothingFound.visibility = View.VISIBLE
                searchBinding.progressBar.visibility = View.GONE
            }

            NetworkError.CONNECTION_ERROR -> {
                searchBinding.searchRecycler.visibility = View.GONE
                searchBinding.nothingFound.visibility = View.GONE
                searchBinding.searchHistoryLayout.visibility = View.GONE
                searchBinding.internetProblem.visibility = View.VISIBLE
                searchBinding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun clearContent() {
        searchBinding.nothingFound.visibility = View.GONE
        searchBinding.internetProblem.visibility = View.GONE
        searchBinding.searchHistoryLayout.visibility = View.GONE
        searchBinding.searchRecycler.visibility = View.GONE
        searchBinding.progressBar.visibility = View.GONE
    }
}