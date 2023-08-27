package com.practicum.playlistmaker_1.media_library.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<FavouriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistFragment)
        }


    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {}
    }
}