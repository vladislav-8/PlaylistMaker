package com.practicum.playlistmaker_1.player.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.player.ui.models.PlayerState
import com.practicum.playlistmaker_1.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import com.practicum.playlistmaker_1.util.EXTRA_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerBinding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        initListeners()

        val track = intent.getSerializableExtra(EXTRA_KEY) as Track
        track.previewUrl?.let { viewModel.prepare(it) }

        viewModel.observeState().observe(this) { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.time.text = getString(R.string.track_time)
                setPlayIcon()
            }
        }

        viewModel.observeTime().observe(this) {
            playerBinding.time.text = it
        }

        showTrack(track)
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

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }

    }

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_play)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}
