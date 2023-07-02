package com.practicum.playlistmaker_1.media_library.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker_1.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FavouriteTracksFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding
    private val viewModel by activityViewModel<FavouriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment().apply {}
    }
}
