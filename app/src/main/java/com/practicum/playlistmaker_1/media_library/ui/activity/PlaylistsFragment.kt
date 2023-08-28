package com.practicum.playlistmaker_1.media_library.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.common.adapters.playlist_adapter.PlaylistsAdapter
import com.practicum.playlistmaker_1.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import com.practicum.playlistmaker_1.media_library.ui.ItemDecorator
import com.practicum.playlistmaker_1.media_library.ui.models.PlaylistsScreenState
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()
    private val playlistsAdapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        viewModel.getPlaylists()

        binding.playlistsGrid.adapter = playlistsAdapter

        binding.playlistsGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsGrid.setHasFixedSize(true)
        binding.playlistsGrid.addItemDecoration(ItemDecorator(40, 0))

        render()
    }

    private fun render() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Filled -> {
                    showPlaylists(state.playlists)
                }

                is PlaylistsScreenState.Empty -> {
                    binding.playlistsGrid.visibility = View.GONE
                    binding.nothingFound.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showPlaylists(playlists: List<PlaylistModel>) {
        playlistsAdapter.clearPlaylists()
        playlistsAdapter.playlists.addAll(playlists)
        binding.playlistsGrid.visibility = View.VISIBLE
        binding.nothingFound.visibility = View.GONE
        playlistsAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {}
    }
}
