package com.practicum.playlistmaker_1.player.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker_1.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerBinding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        track = Gson().fromJson((intent.getStringExtra(TRACK)), Track::class.java)
        initListeners()
        drawPlayer()

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.previewUrl))[PlayerViewModel::class.java]
        viewModel.playState.observe(this) { playState ->
            if (playState) {
                setPauseIcon()
            } else {
                setPlayIcon()
            }
        }

        viewModel.playProgress.observe(this) { duration ->
            playerBinding.time.text = duration
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

        playerBinding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_play)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
    }

    private fun drawPlayer() {
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

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
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

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        const val TRACK = "TRACK"
    }
}
