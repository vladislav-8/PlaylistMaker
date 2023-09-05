package com.practicum.playlistmaker_1.media_library.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker_1.common.adapters.ViewObjects
import com.practicum.playlistmaker_1.common.adapters.playlist_adapter.PlaylistsAdapter
import com.practicum.playlistmaker_1.databinding.FragmentOpenPlaylistBinding
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.OpenPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val viewModel by viewModel<OpenPlaylistViewModel>()

    private val playlistsAdapter by lazy {
        PlaylistsAdapter(viewObject = ViewObjects.VerticalTracks)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initAdapters()

    }

    private fun initListeners() {
        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapters() {
        binding.playlistTracksRv.adapter = playlistsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}