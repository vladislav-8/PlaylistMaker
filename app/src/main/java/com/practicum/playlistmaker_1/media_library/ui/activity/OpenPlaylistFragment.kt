package com.practicum.playlistmaker_1.media_library.ui.activity

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.common.adapters.tracks_adapter.TrackAdapter
import com.practicum.playlistmaker_1.common.util.formatAsMinutes
import com.practicum.playlistmaker_1.databinding.FragmentOpenPlaylistBinding
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.OpenPlaylistViewModel
import com.practicum.playlistmaker_1.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker_1.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<OpenPlaylistViewModel>()

    var playlist: Playlist? = null
    private val tracksAdapter =
        TrackAdapter({ showPlayer(track = it) }, { showLongClickOnTrack(track = it) })

    private val bottomSheetBehavior get() = BottomSheetBehavior.from(binding.bottomSheetSharing).apply {
        state = BottomSheetBehavior.STATE_HIDDEN
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

        initAdapters()
        initListeners()
        initObservers()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.bottomSheetLinear.isVisible = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.bottomSheetLinear.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun initListeners() {
        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistShareIv.setOnClickListener {
            if (playlist?.trackList.equals(getString(R.string.empty_playlist))) {
                Toast.makeText(
                    requireContext(),
                    R.string.you_do_not_have_any_tracks_in_playlist,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val sharePlaylist = viewModel.sharePlaylist(resources)
                viewModel.shareTracks(sharePlaylist)
            }
        }
        binding.playlistMoreMenuIv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
            playlist?.id?.let { playlist_id -> viewModel.getTracks(playlist_id) }
            viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                if (tracks.isEmpty()) {
                    tracksAdapter.tracks = arrayListOf()
                } else {
                    tracksAdapter.tracks = tracks as ArrayList<Track>
                    binding.playlistTimeTv.text = resources.getQuantityString(
                        R.plurals.plural_minutes,
                        tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes().toInt(),
                        tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes()
                    )
                }
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

            binding.playlistTimeTv.text = resources.getQuantityString(
                R.plurals.plural_minutes,
                tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes().toInt(),
                tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes()
            )
        }
    }

    private fun showPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_openPlaylistFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun showLongClickOnTrack(track: Track) {
        showConfirmDialog(track)
    }

    @Deprecated("Deprecated in Java")
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

    private fun showConfirmDialog(track: Track) {
        context?.let { context ->
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(requireContext().getString(R.string.are_you_sure_to_delete_track))
                .setNegativeButton(R.string.no) { dialog, which -> }
                .setPositiveButton(R.string.yes) { dialog, which ->
                    playlist?.let { playlist ->
                        viewModel.deleteTracks(track, playlist)
                        val indexToRemove =
                            tracksAdapter.tracks.indexOfFirst { it.trackId == track.trackId }
                        if (indexToRemove != DELETE_TRACK_FROM_PLAYLIST) {
                            indexToRemove.let { tracksAdapter.tracks.removeAt(it) }
                            indexToRemove.let { tracksAdapter.notifyItemRemoved(it) }
                        }
                    }
                    initObservers()
                }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DELETE_TRACK_FROM_PLAYLIST = -1
    }
}