package com.practicum.playlistmaker_1.player.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.common.util.EXTRA_KEY
import com.practicum.playlistmaker_1.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker_1.player.domain.models.PlayerState
import com.practicum.playlistmaker_1.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _playerBinding: FragmentPlayerBinding? = null
    private val playerBinding get() = _playerBinding!!
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _playerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()

        val track = requireArguments().getSerializable(EXTRA_KEY) as Track

        val bottomSheetBehavior = BottomSheetBehavior.from(playerBinding.bottomSheetLinear).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        playerBinding.overlay.visibility = View.GONE
                    }

                    else -> {
                        playerBinding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        track.previewUrl?.let { viewModel.prepare(it) }

        viewModel.checkIsFavourite(track.trackId)

        playerBinding.likeButton.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }

        playerBinding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        showTrack(track)
    }

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
        playerBinding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }

    private fun initObservers() {
        viewModel.observeIsFavourite().observe(viewLifecycleOwner) { isFavorite ->
            playerBinding.likeButton.setImageResource(
                if (isFavorite) R.drawable.ic_like_button_favourite else R.drawable.like_button
            )
        }
        viewModel.observeTime().observe(viewLifecycleOwner) {
            playerBinding.time.text = it
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.time.text = getString(R.string.track_time)
                setPlayIcon()
            }
        }
    }

    private fun showTrack(track: Track) {
        playerBinding.apply {
            Glide
                .with(mediaTrackImage)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
                .into(mediaTrackImage)
            trackName.text = track.trackName
            artistName.text = track.artistName
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            val date =
                track.releaseDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                        it
                    )
                }
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                releaseDate.text = formattedDatesString
            }
            if (track.collectionName.isNotEmpty()) {
                collectionName.text = track.collectionName
            } else {
                collectionName.visibility = View.GONE
                trackAlbum.visibility = View.GONE
            }
        }
    }

    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                viewModel.play()
                setPauseIcon()
            }

            PlayerState.STATE_PLAYING -> {
                viewModel.pause()
                setPlayIcon()
            }
        }
    }

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_play)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        fun createArgs(track: Track): Bundle {
            return bundleOf(EXTRA_KEY to track)
        }
    }
}