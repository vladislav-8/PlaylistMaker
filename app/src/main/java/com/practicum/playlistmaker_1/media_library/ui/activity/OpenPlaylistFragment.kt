package com.practicum.playlistmaker_1.media_library.ui.activity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.common.adapters.tracks_adapter.TrackAdapter
import com.practicum.playlistmaker_1.common.util.formatAsMinutes
import com.practicum.playlistmaker_1.databinding.FragmentOpenPlaylistBinding
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.OpenPlaylistViewModel
import com.practicum.playlistmaker_1.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker_1.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<OpenPlaylistViewModel>()

    var playlist: Playlist? = null
    private val tracksAdapter = TrackAdapter { showPlayer(track = it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapters() {
        binding.playlistTracksRv.adapter = tracksAdapter
    }

    private fun initObservers() {
        viewModel.playlist.observe(viewLifecycleOwner) {
            showPlaylist(it)
            playlist = it
            Log.d("TAG", "$playlist")
            viewModel.getTracks(playlist?.id!!)
            viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                tracksAdapter.tracks.clear()
                tracksAdapter.tracks.addAll(tracks)
                tracksAdapter.notifyDataSetChanged()
                binding.playlistTimeTv.text = resources.getQuantityString(
                    R.plurals.plural_minutes,
                    tracks.sumOf { it.trackTimeMillis }.formatAsMinutes().toInt(),
                    tracks.sumOf { it.trackTimeMillis }.formatAsMinutes())
            }
        }
    }

    private fun showPlaylist(playlist: Playlist) {

        with(binding) {

            if (playlist.imageUri.toString() != "null") {
                playlistImage.setImageURI(playlist.imageUri)
            } else {
                context?.let {
                    Glide
                        .with(it)
                        .load(R.drawable.placeholder)
                        .transform(CenterInside())
                        .into(playlistImage)
                }
            }

            playlistTitle.text = playlist.title
            playlistDescription.text = playlist.description
            playlistSize.text = resources.getQuantityString(
                R.plurals.plural_tracks,
                R.string.playlists,
                playlist.size
            )
        }
    }

    private fun showPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_openPlaylistFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val sourceTreeUri = data?.data
            if (sourceTreeUri != null) {
                context?.contentResolver?.takePersistableUriPermission(
                    sourceTreeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}