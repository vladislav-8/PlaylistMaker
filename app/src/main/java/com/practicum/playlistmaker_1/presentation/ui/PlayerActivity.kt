package com.practicum.playlistmaker_1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.creator.Creator
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.domain.models.Track
import com.practicum.playlistmaker_1.presentation.player.PlayerView
import com.practicum.playlistmaker_1.presentation.player.presenter.PlayerPresenter
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var presenter: PlayerPresenter

    private val playerBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerBinding.root)

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        presenter = Creator.providePresenter(
            view = this,
            track = track
        )

        initListeners()
        drawPlayer(track)
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
            presenter.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_play)
    }

    override fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
    }

    override fun drawPlayer(track: Track) {
        Glide
            .with(playerBinding.mediaTrackImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(playerBinding.mediaTrackImage)

        playerBinding.trackName.text = track.trackName
        playerBinding.artistName.text = track.artistName
        playerBinding.primaryGenreName.text = track.primaryGenreName
        playerBinding.country.text = track.country

        playerBinding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            playerBinding.releaseDate.text = formattedDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            playerBinding.collectionName.text = track.collectionName
        } else {
            playerBinding.collectionName.visibility = View.GONE
            playerBinding.trackAlbum.visibility = View.GONE
        }
    }

    override fun setDuration(ms: String) {
        playerBinding.time.text = ms
    }
}
